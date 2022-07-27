package com.rc.pay.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rc.pay.constants.PayConstants;
import com.rc.pay.wechat.WechatCertUtil;
import com.rc.pay.util.JsonUtil;
import com.rc.pay.wechat.WechatHttpUtil;
import com.rc.pay.wechat.WechatSignUtil;
import com.rc.pay.wechat.model.Wechat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: RainCity
 * @Date: 2021-11-01 14:04:37
 * @Desc: 微信支付调用 回调示例
 */
public class WechatPayController {
    private static final Logger log = LoggerFactory.getLogger(WechatPayController.class);


    public JSONObject getOpenId() {
        log.info("********** 获取微信openId==> *************");
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            return JsonUtil.parseObject(result);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("请求失败：{}",e.getMessage());
            return null;
        }
    }

    //@PostMapping("/wechat_notify")
    public String wechatNotify(HttpServletRequest request) {
        log.info("********** 微信支付回调执行 *************");
        //微信返回的请求体
        String body = WechatHttpUtil.getRequestBody(request);
        if(null == body){
            return PayConstants.WECHAT_FAILED_RETURN;
        }
        //商户证书序列号
        String serialNo = request.getHeader(PayConstants.WECHAT_PAY_SERIAL);
        // TODO: 2021/11/3 通过证书序列号查询
        Wechat wechat = new Wechat();
        if(!WechatSignUtil.verifiedSign(request, body, wechat.getMchId(), wechat.getSerialNumber(),
                wechat.getPrivateKeyUrl(), wechat.getApiKey())){
            return PayConstants.WECHAT_FAILED_RETURN;
        }
        JSONObject bodyJo = JSON.parseObject(body);
        String resourceType = bodyJo.getString("resource_type");
        String eventType = bodyJo.getString("event_type");
        if(!PayConstants.RESOURCE_TYPE.equals(resourceType) || !PayConstants.EVENT_TYPE.equals(eventType)){
            return PayConstants.WECHAT_FAILED_RETURN;
        }
        JSONObject encrypt = bodyJo.getJSONObject("resource");
        String algorithm = encrypt.getString("algorithm");
        String ciphertext = encrypt.getString("ciphertext");
        String associatedData = encrypt.getString("associated_data");
        String originalType = encrypt.getString("original_type");
        String nonce = encrypt.getString("nonce");
        String decrypt = WechatCertUtil.decryptResBody(wechat.getApiKey(), associatedData, nonce, ciphertext);
        JSONObject resource = JSON.parseObject(decrypt);
        //业务逻辑
        int num = 0;
        // TODO: 2021/11/4 向数据库中加入支付记录
        return num > 0 ? PayConstants.WECHAT_SUCCESS_RETURN : PayConstants.WECHAT_FAILED_RETURN;
    }

}
