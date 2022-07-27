package com.rc.pay.wechat.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 15:05:05
 * @Desc: 微信支付 实体
 */
public class WechatPay extends Wechat {
    private static final long serialVersionUID = 7121881760192226056L;

    /**应用ID*/
    private String appId;
    /**appSecret*/
    private String appSecret;

    /** 微信个人唯一标识 */
    private String openId;

    private String clientIp;

    /**商品描述*/
    private String description;

    /**支付金额 */
    private BigDecimal totalAmount;
    /**
     * 交易绝对时间
     * 支付宝格式：yyyy-MM-dd HH:mm:ss
     * 微信格式：YYYY-MM-DDTHH:mm:ss+TIMEZONE
     */
    private String timeExpire;
    /**支付回调地址*/
    private String notifyUrl;
    /**支付跳转地址*/
    private String returnUrl;
    /**支付时的附加参数*/
    private String attach;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("appId", appId)
                .append("appSecret", appSecret)
                .append("openId", openId)
                .append("clientIp", clientIp)
                .append("description", description)
                .append("totalAmount", totalAmount)
                .append("timeExpire", timeExpire)
                .append("notifyUrl", notifyUrl)
                .append("returnUrl", returnUrl)
                .append("attach", attach)
                .toString() + super.toString();
    }
}
