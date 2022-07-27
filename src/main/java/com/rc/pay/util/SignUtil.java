package com.rc.pay.util;

import com.rc.pay.constants.PayConstants;
import com.rc.pay.constants.WechatConstants;
import com.rc.pay.wechat.WechatCertUtil;
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
 * @Desc: 签名工具类
 */
public class SignUtil {

    private SignUtil() {
    }

    /**
     * 获取随机字符串 Nonce Str
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        Random random = new SecureRandom();
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = WechatConstants.SYMBOLS.charAt(random.nextInt(WechatConstants.SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    public static String sign(byte[] message,PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(WechatConstants.SHA256_WITH_RSA);
        sign.initSign(privateKey);
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    /**
     * 根据地址获取私钥
     * @param keyUrl 私钥地址
     * @return java.security.PrivateKey
     */
    public static PrivateKey getPrivateKey(String keyUrl) throws IOException {
        //读取商户私钥
        URL url = new URL(keyUrl);
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        return PemUtil.loadPrivateKey(in);
    }

    /**
     * 签名
     * @param algorithm 签名算法
     * @param privateKey 私钥
     * @param data 带签数据
     * @return java.lang.String
     */
    public static String sign(String algorithm, PrivateKey privateKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 验签
     * @param algorithm 签名算法
     * @param certificate 证书
     * @param data 待验数据
     * @return java.lang.Boolean
     */
    public static Boolean verify(String algorithm, X509Certificate certificate, String signStr, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(certificate);
        signature.update(signStr.getBytes());
        //返回验签结果
        return signature.verify(Base64.getDecoder().decode(data));
    }

    public static String sha1Sign(String str) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
