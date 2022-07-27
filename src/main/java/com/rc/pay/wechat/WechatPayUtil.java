package com.rc.pay.wechat;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rc.pay.constants.PayConstants;
import com.rc.pay.constants.WechatConstants;
import com.rc.pay.util.JsonUtil;
import com.rc.pay.wechat.model.Wechat;
import com.rc.pay.wechat.model.WechatPay;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: RainCity
 * @Date: 2021-11-02 11:06:10
 * @Desc: 微信支付相关工具类
 */
public class WechatPayUtil {
    private static final Logger log = LoggerFactory.getLogger(WechatPayUtil.class);

    private static CloseableHttpClient httpClient;

    private static HttpPost httpPost;

    private WechatPayUtil() {
    }

    /**
     * 初始化http
     * @param wechat 请求参数
     * @see Wechat#toString()
     * @param url 请求地址
     * @throws FileNotFoundException
     */
    private static void initHttp(Wechat wechat, String url) throws IOException, NotFoundException, GeneralSecurityException, HttpCodeException {
        //通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        httpClient = WechatHttpUtil.initHttpClient(wechat.getMchId(), wechat.getSerialNumber(), wechat.getPrivateKeyUrl(), wechat.getApiKey());

        httpPost = WechatHttpUtil.initHttpPost(url);
    }

    /**
     * 参数封装
     * @param wechatPay 请求参数
     * @see WechatPay#toString()
     * @see Wechat#toString()
     * @return ObjectNode
     */
    public static ObjectNode encapsulation(ObjectMapper objectMapper, WechatPay wechatPay){
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode
                //应用ID
                .put("appid", wechatPay.getAppId())
                //直连商户号
                .put("mchid",wechatPay.getMchId())
                //商品描述
                .put("description", wechatPay.getDescription())
                //通知地址
                .put("notify_url", wechatPay.getNotifyUrl())
                //商户订单号
                .put("out_trade_no", wechatPay.getOutTradeNo());
        //附加数据
        if(StringUtils.isNotBlank(wechatPay.getAttach())){
            rootNode.put("attach",wechatPay.getAttach());
        }
        //订单失效时间,格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE,建议：最短失效时间间隔大于1分钟
        if(StringUtils.isNotBlank(wechatPay.getTimeExpire())){
            rootNode.put(WechatConstants.TIME_EXPIRE,wechatPay.getTimeExpire());
        }

        //订单金额
        BigDecimal bigDecimal = wechatPay.getTotalAmount();
        int totalAmount = bigDecimal.multiply(new BigDecimal(100)).intValue();
        rootNode.putObject("amount")
                //总金额
                .put("total", totalAmount)
                //货币类型  默认CNY
                .put("currency","CNY");
        return rootNode;
    }

    /**
     * JSAPI、小程序下单
     * @param wechatPay 请求参数
     * @see WechatPay#toString()
     * @return prepay_id 预支付交易会话标识。用于后续接口调用中使用,该值有效期为2小时
     */
    public static String transactionsJsApi(WechatPay wechatPay) throws IOException, GeneralSecurityException, NotFoundException, HttpCodeException {
        CloseableHttpClient httpClient = WechatHttpUtil.initHttpClient(wechatPay.getMchId(),
                wechatPay.getSerialNumber(), wechatPay.getPrivateKeyUrl(), wechatPay.getApiKey());

        //请求参数封装
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = encapsulation(objectMapper, wechatPay);
        //支付者
        rootNode.putObject("payer")
                //用户标识
                .put("openid", wechatPay.getOpenId());
        objectMapper.writeValue(bos, rootNode);

        HttpPost httpPost = WechatHttpUtil.initHttpPost(WechatConstants.JSAPI_URL);
        httpPost.setEntity(new StringEntity(bos.toString(PayConstants.UTF_8), PayConstants.UTF_8));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        payLog(PayConstants.JSAPI, bodyAsString);

        httpClient.close();
        JSONObject result = JsonUtil.parseObject(bodyAsString);
        return null != result  ? result.getString("prepay_id") : null;
    }

    /**
     * H5下单
     * @param wechatPay 请求参数
     * @see WechatPay#toString()
     * @return 支付跳转链接。h5_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付，h5_url的有效期为5分钟
     */
    public static String transactionsH5(WechatPay wechatPay) throws IOException, GeneralSecurityException, NotFoundException, HttpCodeException {
        CloseableHttpClient httpClient = WechatHttpUtil.initHttpClient(wechatPay.getMchId(),
                wechatPay.getSerialNumber(), wechatPay.getPrivateKeyUrl(), wechatPay.getApiKey());

        //请求参数封装
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = encapsulation(objectMapper, wechatPay);
        //场景信息
        rootNode.putObject("scene_info")
                //用户终端IP
                .put("payer_client_ip", wechatPay.getClientIp())
                //H5场景信息
                .putObject("h5_info")
                //场景类型
                .put("type", "Wap");

        objectMapper.writeValue(bos, rootNode);

        HttpPost httpPost = WechatHttpUtil.initHttpPost(WechatConstants.H5_URL);
        httpPost.setEntity(new StringEntity(bos.toString(PayConstants.UTF_8), PayConstants.UTF_8));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String bodyAsString = EntityUtils.toString(response.getEntity());
        payLog(PayConstants.H5, bodyAsString);

        httpClient.close();
        JSONObject result = JsonUtil.parseObject(bodyAsString);
        return null != result  ? result.getString("h5_url") : null;
    }


    /**
     * APP下单
     * @param wechatPay 请求参数
     * @see WechatPay#toString()
     * @return prepay_id 预支付交易会话标识。用于后续接口调用中使用,该值有效期为2小时
     */
    public static String transactionsApp(WechatPay wechatPay) throws IOException, GeneralSecurityException, NotFoundException, HttpCodeException {
        CloseableHttpClient httpClient = WechatHttpUtil.initHttpClient(wechatPay.getMchId(),
                wechatPay.getSerialNumber(), wechatPay.getPrivateKeyUrl(), wechatPay.getApiKey());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = encapsulation(objectMapper, wechatPay);
        objectMapper.writeValue(bos, rootNode);

        HttpPost httpPost = WechatHttpUtil.initHttpPost(WechatConstants.APP_URL);
        httpPost.setEntity(new StringEntity(bos.toString(PayConstants.UTF_8), PayConstants.UTF_8));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        payLog(PayConstants.APP, bodyAsString);

        httpClient.close();
        JSONObject result = JsonUtil.parseObject(bodyAsString);
        return null != result  ? result.getString("prepay_id") : null;
    }


    /**
     * Native下单
     * @param wechatPay 请求参数
     * @see WechatPay#toString()
     * @return 二维码链接。code_url并非固定值，使用时按照URL格式转成二维码即可
     */
    public static String transactionsNative(WechatPay wechatPay) throws IOException, GeneralSecurityException, NotFoundException, HttpCodeException {
        CloseableHttpClient httpClient = WechatHttpUtil.initHttpClient(wechatPay.getMchId(),
                wechatPay.getSerialNumber(), wechatPay.getPrivateKeyUrl(), wechatPay.getApiKey());

        //请求参数封装
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = encapsulation(objectMapper, wechatPay);
        objectMapper.writeValue(bos, rootNode);

        HttpPost httpPost = WechatHttpUtil.initHttpPost(WechatConstants.NATIVE_URL);
        httpPost.setEntity(new StringEntity(bos.toString(PayConstants.UTF_8), PayConstants.UTF_8));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        payLog(PayConstants.NATIVE, bodyAsString);

        httpClient.close();
        JSONObject result = JsonUtil.parseObject(bodyAsString);
        return null != result  ? result.getString("code_url") : null;
    }

    /**
     * 订单查询
     * @param wechat 请求参数
     * @see Wechat#toString()
     * @return 订单结果
     */
    public static JSONObject transactionsQuery(Wechat wechat) throws URISyntaxException, IOException, GeneralSecurityException, NotFoundException, HttpCodeException {
        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        CloseableHttpClient httpClient = WechatHttpUtil.initHttpClient(wechat.getMchId(),
                wechat.getSerialNumber(), wechat.getPrivateKeyUrl(), wechat.getApiKey());

        String url = WechatConstants.QUERY_URL.replace("OUTTRADENO",wechat.getOutTradeNo())
                .replace("MERCHANTID",wechat.getMchId());
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");

        CloseableHttpResponse response = httpClient.execute(httpGet);
        String bodyAsString = EntityUtils.toString(response.getEntity());
        log.info("微信订单查询:{}",bodyAsString);
        return JsonUtil.parseObject(bodyAsString);
    }

    /**
     * 微信订单关闭
     * @param wechat 请求参数
     * @see Wechat#toString()
     * @return
     */
    public static JSONObject transactionsClose(Wechat wechat) throws IOException, GeneralSecurityException, NotFoundException, HttpCodeException {
        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        CloseableHttpClient httpClient = WechatHttpUtil.initHttpClient(wechat.getMchId(),
                wechat.getSerialNumber(), wechat.getPrivateKeyUrl(), wechat.getApiKey());

        String url = WechatConstants.CLOSE_URL.replace("OUTTRADENO",wechat.getOutTradeNo());
        HttpPost httpPost = WechatHttpUtil.initHttpPost(url);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid",wechat.getMchId());

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString(PayConstants.UTF_8), PayConstants.UTF_8));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        log.info("微信订单关闭==response==getStatusCode：{}",response.getStatusLine().getStatusCode());
        String bodyAsString = EntityUtils.toString(response.getEntity());
        log.info("微信订单关闭：{}",bodyAsString);
        if(204 == response.getStatusLine().getStatusCode()){
            return JsonUtil.success("订单关闭成功");
        }
        return JsonUtil.fail("订单关闭失败");
    }

    private static void payLog(String payWay, String result){
        log.info("支付方式：{}，统一下单接口:{}",payWay,result);
    }

    /**
     * 设置过期时间
     * @param minute 几分钟后过期
     * @return java.lang.String
     */
    public static String timeExpire(int minute){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusMinutes(minute);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
        return localDateTime.format(formatter);
    }

}
