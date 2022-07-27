package com.rc.pay.wechat.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 15:05:05
 * @Desc: 微信支付 实体
 */
public class Wechat implements Serializable {

    private static final long serialVersionUID = -8579377035085626830L;
    /**直连商户号*/
    private String mchId;
    /**商户API证书的证书序列号*/
    private String serialNumber;
    /**API v3密钥*/
    private String apiKey;
    /**商户私钥存储地址*/
    private String privateKeyUrl;

    /**订单号*/
    private String outTradeNo;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPrivateKeyUrl() {
        return privateKeyUrl;
    }

    public void setPrivateKeyUrl(String privateKeyUrl) {
        this.privateKeyUrl = privateKeyUrl;
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
                .append("mchId", mchId)
                .append("serialNumber", serialNumber)
                .append("apiKey", apiKey)
                .append("privateKeyUrl", privateKeyUrl)
                .append("outTradeNo", outTradeNo)
                .toString();
    }
}
