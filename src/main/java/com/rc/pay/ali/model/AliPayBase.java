package com.rc.pay.ali.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 14:32:29
 * @Desc:
 */
public class AliPayBase implements Serializable {
    private static final long serialVersionUID = 596139540151968484L;

    /** 商户 appId */
    String appId;
    /** 商户私钥 */
    String privateKey;
    /** 支付宝公钥 */
    String publicKey;
    /** 商户订单号 */
    String outTradeNo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("appId", appId)
                .append("privateKey", privateKey)
                .append("publicKey", publicKey)
                .append("outTradeNo", outTradeNo)
                .toString();
    }
}
