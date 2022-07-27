package com.rc.pay.wechat;

import com.rc.pay.constants.PayConstants;
import com.rc.pay.constants.WechatConstants;
import com.rc.pay.util.SignUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: RainCity
 * @Date: 2021-11-03 16:00:17
 * @Desc: 微信支付签名相关
 */
public class WechatSignUtil {
    private static final Logger log = LoggerFactory.getLogger(WechatSignUtil.class);

    private WechatSignUtil() {
    }

    public static Boolean verifiedSign(HttpServletRequest request, String body, String mchId,
                                       String serialNumber, String privateKeyUrl, String apiKey) {
        log.info("商户证书序列号：{}",serialNumber);
        //平台证书序列号
        String serialNo = request.getHeader(PayConstants.WECHAT_PAY_SERIAL);
        log.info("平台证书序列号：{}",serialNo);
        //应答时间戳
        String timestamp = request.getHeader(PayConstants.WECHAT_PAY_TIMESTAMP);
        //应答随机串
        String nonceStr = request.getHeader(PayConstants.WECHAT_PAY_NONCE);
        //应答签名
        String wechatSign = request.getHeader(PayConstants.WECHAT_PAY_SIGNATURE);
        //组装签名字符串
        String signStr = Stream.of(timestamp, nonceStr, body)
                .collect(Collectors.joining("\n", "", "\n"));

        try {
            //String nonceStr = SignUtil.generateNonceStr();
            //long timestamp = System.currentTimeMillis() / 1000;
            String message = Stream.of("GET", WechatConstants.CERT_ABSOLUTE_URL, String.valueOf(timestamp),nonceStr,"")
                    .collect(Collectors.joining("\n", "", "\n"));
            log.info("获取平台证书请求签名串：{}",message);
            //读取商户私钥
            PrivateKey privateKey = SignUtil.getPrivateKey(privateKeyUrl);
            String sign = SignUtil.sign(message.getBytes(StandardCharsets.UTF_8),privateKey);
            log.info("获取平台证书签名值：{}",sign);
            String token = WechatConstants.SCHEMA+" mchid=\"" + mchId + "\","
                    + "nonce_str=\"" + nonceStr + "\","
                    + "timestamp=\"" + timestamp + "\","
                    + "serial_no=\"" + serialNumber + "\","
                    + "signature=\"" + sign + "\"";
            log.info("获取平台证书token：{}",token);
            //根据序列号获取平台证书
            X509Certificate certificate = WechatCertUtil.getWechatCertificate(token,serialNo,apiKey);
            //获取失败 验证失败
            if (certificate == null){
                return false;
            }
            //返回验签结果
            return SignUtil.verify(WechatConstants.SHA256_WITH_RSA, certificate, signStr, wechatSign);
        } catch (URISyntaxException | CertificateException | IOException e) {
            log.error("证书获取失败====>");
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            log.error("{}算法不匹配====>",WechatConstants.SHA256_WITH_RSA);
            e.printStackTrace();
            return false;
        } catch (SignatureException | InvalidKeyException e) {
            log.error("签名错误====>");
            e.printStackTrace();
            return false;
        }
    }

}
