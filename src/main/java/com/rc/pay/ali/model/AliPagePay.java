package com.rc.pay.ali.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 14:16:51
 * @Desc: 电脑网站支付 实体
 */
public class AliPagePay extends AliAppPay{
    private static final long serialVersionUID = 2603130118955861493L;

    /** 网页重定向通知 HTTP/HTTPS开头字符串 */
    private String returnUrl;

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("returnUrl", returnUrl)
                .toString() + super.toString();
    }
}
