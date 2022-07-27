package com.rc.pay.ali;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.rc.pay.ali.model.*;
import com.rc.pay.constants.AliConstants;
import com.rc.pay.constants.PayConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: RainCity
 * @Date: 2021-11-01 15:17:50
 * @Desc: 支付宝支付工具类
 */
public class AliPayUtil {
    private static final Logger log = LoggerFactory.getLogger(AliPayUtil.class);

    private AliPayUtil() {
    }

    /**
     * 扫码支付
     * @param aliPay 请求参数
     * @see AliPay#toString()
     * @return com.alipay.api.response.AlipayTradePrecreateResponse
     * @see AlipayTradePrecreateResponse#getQrCode() 收款码
     * @see AlipayTradePrecreateResponse#getOutTradeNo()
     */
    public static AlipayTradePrecreateResponse tradePreCreate(AliPay aliPay) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, aliPay.getAppId(), aliPay.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, aliPay.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        //设置回调地址
        request.setNotifyUrl(aliPay.getNotifyUrl());

        //请求参数的集合
        JSONObject bizContent = new JSONObject();
        //商户订单号
        bizContent.put(AliConstants.OUT_TRADE_NO,aliPay.getOutTradeNo());
        //订单总金额，单位为元，精确到小数点后两位
        bizContent.put(AliConstants.TOTAL_AMOUNT,aliPay.getTotalAmount());
        //订单标题
        bizContent.put(AliConstants.SUBJECT,aliPay.getSubject());
        // 公用回传参数，按需传入
        if(StringUtils.isNotBlank(aliPay.getAttach())){
            bizContent.put(AliConstants.PASS_BACK_PARAMS, aliPay.getAttach());
        }

        request.setBizContent(bizContent.toString());
        AlipayTradePrecreateResponse response = alipayClient.execute (request);
        log.info("response:{}",response);
        return response;
    }

    /**
     * 付款码支付接口
     * @param codePay 请求参数
     * @see AliCodePay#toString()
     * @see AliPay#toString()
     * @return com.alipay.api.response.AlipayTradePayResponse
     * @see AlipayTradePayResponse#getOutTradeNo()
     * @see AlipayTradePayResponse#getTotalAmount()
     * @see AlipayTradePayResponse#getReceiptAmount()
     */
    public static AlipayTradePayResponse tradePayBarCode(AliCodePay codePay) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, codePay.getAppId(), codePay.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, codePay.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        //设置回调地址
        request.setNotifyUrl(codePay.getNotifyUrl());

        JSONObject bizContent = new JSONObject();
        //商户订单号
        bizContent.put(AliConstants.OUT_TRADE_NO,codePay.getOutTradeNo());
        //订单总金额，单位为元，精确到小数点后两位
        bizContent.put(AliConstants.TOTAL_AMOUNT,codePay.getTotalAmount());
        //订单标题
        bizContent.put(AliConstants.SUBJECT,codePay.getSubject());
        //支付场景 bar_code：当面付条码支付场景; security_code：当面付刷脸支付场景，对应的auth_code为fp开头的刷脸标识串；
        bizContent.put("scene", codePay.getScene());
        //支付授权码 当面付场景传买家的付款码
        bizContent.put("auth_code", codePay.getAuthCode());
        // 公用回传参数，按需传入
        if(StringUtils.isNotBlank(codePay.getAttach())){
            bizContent.put(AliConstants.PASS_BACK_PARAMS, codePay.getAttach());
        }

        request.setBizContent(bizContent.toString());
        AlipayTradePayResponse response = alipayClient.execute(request);
        log.info("TradePayResponse:{}",response);
        return  response;
    }

    /**
     * APP支付
     * @param appPay 请求参数
     * @see AliAppPay#toString()
     * @see AliPay#toString()
     * @return 表单格式，可嵌入页面，具体以返回的结果为准
     * @see AlipayTradeAppPayResponse#getBody()
     */
    public static AlipayTradeAppPayResponse tradeAppPay(AliAppPay appPay) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, appPay.getAppId(), appPay.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, appPay.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //设置回调地址
        request.setNotifyUrl(appPay.getNotifyUrl());

        JSONObject bizContent = new JSONObject();
        //商户网站唯一订单号
        bizContent.put(AliConstants.OUT_TRADE_NO, appPay.getOutTradeNo());
        //订单总金额，单位为元
        bizContent.put(AliConstants.TOTAL_AMOUNT,appPay.getTotalAmount());
        //订单标题
        bizContent.put(AliConstants.SUBJECT,appPay.getSubject());
        //销售产品码，商家和支付宝签约的产品码
        bizContent.put(AliConstants.PRODUCT_CODE, "QUICK_MSECURITY_PAY");
        //绝对超时时间，格式为yyyy-MM-dd HH:mm:ss
        if(StringUtils.isNotBlank(appPay.getTimeExpire())){
            bizContent.put(AliConstants.TIME_EXPIRE, appPay.getTimeExpire());
        }
        // 公用回传参数，按需传入
        if(StringUtils.isNotBlank(appPay.getAttach())){
            bizContent.put(AliConstants.PASS_BACK_PARAMS, appPay.getAttach());
        }

        request.setBizContent(bizContent.toString());
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        log.info("TradeAppPayResponse:{}",response);
        return  response;
    }

    /**
     * 手机网站支付
     * @param wapPay 请求参数
     * @see AliWapPay#toString()
     * @see AliPay#toString()
     * @return 表单格式，可嵌入页面，具体以返回的结果为准
     * @see AlipayTradeWapPayResponse#getBody()
     */
    public static AlipayTradeWapPayResponse tradeWapPay(AliWapPay wapPay) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, wapPay.getAppId(), wapPay.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, wapPay.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        //设置回调地址
        request.setNotifyUrl(wapPay.getNotifyUrl());
        //网页重定向通知 HTTP/HTTPS开头字符串
        request.setReturnUrl(wapPay.getReturnUrl());

        JSONObject bizContent = new JSONObject();
        //商户网站唯一订单号
        bizContent.put(AliConstants.OUT_TRADE_NO, wapPay.getOutTradeNo());
        //订单总金额，单位为元
        bizContent.put(AliConstants.TOTAL_AMOUNT,wapPay.getTotalAmount());
        //订单标题
        bizContent.put(AliConstants.SUBJECT,wapPay.getSubject());
        //销售产品码，商家和支付宝签约的产品码
        bizContent.put(AliConstants.PRODUCT_CODE, "QUICK_WAP_WAY");
        //绝对超时时间，格式为yyyy-MM-dd HH:mm:ss
        if(StringUtils.isNotBlank(wapPay.getTimeExpire())){
            bizContent.put(AliConstants.TIME_EXPIRE, wapPay.getTimeExpire());
        }
        // 公用回传参数，按需传入
        if(StringUtils.isNotBlank(wapPay.getAttach())){
            bizContent.put(AliConstants.PASS_BACK_PARAMS, wapPay.getAttach());
        }
        //用户付款中途退出返回商户网站的地址
        if(StringUtils.isNotBlank(wapPay.getQuitUrl())){
            bizContent.put("quit_url", wapPay.getQuitUrl());
        }

        request.setBizContent(bizContent.toString());
        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
        log.info("TradeWapPayResponse:{}",response);
        return response;
    }

    /**
     * 电脑网站支付，PC场景下单并支付
     * @param pagePay 请求参数
     * @see AliPagePay#toString()
     * @see AliPay#toString()
     * @return 表单格式，可嵌入页面，具体以返回的结果为准
     * @see AlipayTradePagePayResponse#getBody()
     */
    public static AlipayTradePagePayResponse tradePagePay(AliPagePay pagePay) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, pagePay.getAppId(), pagePay.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, pagePay.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //设置回调地址
        request.setNotifyUrl(pagePay.getNotifyUrl());
        //网页重定向通知 HTTP/HTTPS开头字符串
        request.setReturnUrl(pagePay.getReturnUrl());

        JSONObject bizContent = new JSONObject();
        //商户网站唯一订单号
        bizContent.put(AliConstants.OUT_TRADE_NO, pagePay.getOutTradeNo());
        //订单总金额，单位为元
        bizContent.put(AliConstants.TOTAL_AMOUNT,pagePay.getTotalAmount());
        //订单标题
        bizContent.put(AliConstants.SUBJECT,pagePay.getSubject());
        //销售产品码，与支付宝签约的产品码名称
        bizContent.put(AliConstants.PRODUCT_CODE, "FAST_INSTANT_TRADE_PAY");
        //PC扫码支付的方式
        bizContent.put("qr_pay_mode", "2");
        //绝对超时时间，格式为yyyy-MM-dd HH:mm:ss
        if(StringUtils.isNotBlank(pagePay.getTimeExpire())){
            bizContent.put(AliConstants.TIME_EXPIRE, pagePay.getTimeExpire());
        }
        // 公用回传参数，按需传入
        if(StringUtils.isNotBlank(pagePay.getAttach())){
            bizContent.put(AliConstants.PASS_BACK_PARAMS, pagePay.getAttach());
        }

        request.setBizContent(bizContent.toString());

        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        log.info("TradePagePayResponse:{}",response);
        return response;
    }

    /**
     * 交易查询
     * @param payBase 请求参数
     * @see  AliPayBase#toString()
     * @return 交易状态码
     */
    public static String tradeQuery(AliPayBase payBase){
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, payBase.getAppId(), payBase.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, payBase.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        JSONObject bizContent = new JSONObject();
        //商户网站唯一订单号
        bizContent.put(AliConstants.OUT_TRADE_NO, payBase.getOutTradeNo());

        request.setBizContent(bizContent.toString());

        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            log.info("TradeQueryResponse:{}",response);
            if(response.isSuccess()){
                return response.getTradeStatus();
            }
            return null;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("订单查询失败：errCode:{},errMsg:{}",e.getErrCode(),e.getErrMsg());
            return null;
        }
    }

    /**
     * 交易撤销
     * @param payBase 请求参数
     * @see AliPayBase#toString()
     * @return retryFlag=是否需要重试(Y/N)
     * action=本次撤销触发的交易动作,接口调用成功且交易存在时返回
     *          可能的返回值：
     *          close：交易未支付，触发关闭交易动作，无退款；
     *          refund：交易已支付，触发交易退款动作；
     *          未返回：未查询到交易，或接口调用失败；
     * @see AlipayTradeCancelResponse#isSuccess()
     */
    public static AlipayTradeCancelResponse tradeCancel(AliPayBase payBase){
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, payBase.getAppId(), payBase.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, payBase.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();

        JSONObject bizContent = new JSONObject();
        //商户网站唯一订单号
        bizContent.put(AliConstants.OUT_TRADE_NO, payBase.getOutTradeNo());
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            log.info("TradeCancelResponse:{}",response);
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("订单撤销失败：errCode:{},errMsg:{}",e.getErrCode(),e.getErrMsg());
            return null;
        }
    }

    /**
     * 交易关闭
     * 用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭
     * @param payBase 请求参数
     * @see  AliPayBase#toString()
     * @return com.alipay.api.response.AlipayTradeCloseResponse
     * @see AlipayTradeCloseResponse#isSuccess()
     */
    public static AlipayTradeCloseResponse tradeClose(AliPayBase payBase){
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.GATEWAY_URL, payBase.getAppId(), payBase.getPrivateKey(),
                PayConstants.FORMAT_JSON, PayConstants.UTF_8, payBase.getPublicKey(), AliConstants.SIGN_TYPE);
        //创建API对应的request类
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();

        JSONObject bizContent = new JSONObject();
        //商户网站唯一订单号
        bizContent.put(AliConstants.OUT_TRADE_NO, payBase.getOutTradeNo());
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            log.info("TradeCloseResponse:{}",response);
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("订单关闭失败：errCode:{},errMsg:{}",e.getErrCode(),e.getErrMsg());
            return null;
        }
    }

    /**
     * 获取支付宝POST过来反馈信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String,String> getPostParams(HttpServletRequest request){
        Map<String,String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 设置过期时间
     * @param minute 几分钟后过期
     * @return java.lang.String
     */
    public static String timeExpire(int minute){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusMinutes(minute);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
