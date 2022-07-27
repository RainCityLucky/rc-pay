package com.rc.pay.ali.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 13:47:33
 * @Desc: 支付宝支付参数
 */
public class AliPay extends AliPayBase {

    private static final long serialVersionUID = -4714606061259237774L;

    /** 回调地址 */
    String notifyUrl;
    /**  订单总金额，单位为元，精确到小数点后两位 */
    String totalAmount;
    /** 订单标题 */
    String subject;
    /** 公用回传参数，按需传入 */
    String attach;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
                .append("notifyUrl", notifyUrl)
                .append("totalAmount", totalAmount)
                .append("subject", subject)
                .append("attach", attach)
                .toString() + super.toString();
    }
}
