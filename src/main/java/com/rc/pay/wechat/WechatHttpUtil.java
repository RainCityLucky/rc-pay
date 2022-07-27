package com.rc.pay.wechat;

import com.rc.pay.util.SignUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.*;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

/**
 * @Author: RainCity
 * @Date: 2021-11-03 17:32:28
 * @Desc:
 */
public class WechatHttpUtil {
    private static final Logger log = LoggerFactory.getLogger(WechatHttpUtil.class);

    private WechatHttpUtil() {
    }

    /**
     * 初始化 HttpClient
     * @param mchId 商户号
     * @param serialNumber 商户序列号
     * @param privateKeyUrl 私钥地址
     * @param apiKey Api v3Key
     * @return
     * @throws FileNotFoundException
     */
    public static CloseableHttpClient initHttpClient(String mchId, String serialNumber, String privateKeyUrl, String apiKey) throws IOException, GeneralSecurityException, HttpCodeException, NotFoundException {
        //读取商户私钥
        PrivateKey privateKey = SignUtil.getPrivateKey(privateKeyUrl);
        //私钥签名
        PrivateKeySigner signer = new PrivateKeySigner(serialNumber, privateKey);
        //获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        //向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId, signer), apiKey.getBytes(StandardCharsets.UTF_8));

        //从证书管理器中获取verifier
        Verifier verifier = certificatesManager.getVerifier(mchId);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, serialNumber, privateKey)
                .withValidator(new WechatPay2Validator(verifier));

        //通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        return builder.build();
    }

    /**
     * 初始化 HttpPost
     * @param uri 请求地址
     * @return
     */
    public static HttpPost initHttpPost(String uri){
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");
        return httpPost;
    }

    /**
     * 获取 HttpServletRequest body
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request){
        ServletInputStream stream = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            stream = request.getInputStream();
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }catch (IOException e){
            log.error("读取返回支付接口数据流出现异常");
            return null;
        }finally {
            try {
                if(null != reader){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
