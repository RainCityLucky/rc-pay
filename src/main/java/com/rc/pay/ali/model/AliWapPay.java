package com.rc.pay.ali.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 14:24:52
 * @Desc: 手机网站支付 实体
 */
public class AliWapPay extends AliPagePay{
    private static final long serialVersionUID = -3950451388545915432L;

    /** 用户付款中途退出返回商户网站的地址 */
    private String quitUrl;

    public String getQuitUrl() {
        return quitUrl;
    }

    public void setQuitUrl(String quitUrl) {
        this.quitUrl = quitUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("quitUrl", quitUrl)
                .toString() + super.toString();
    }
}
