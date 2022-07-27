package com.rc.pay.ali.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 14:09:51
 * @Desc: APP支付 实体
 */
public class AliAppPay extends AliPay {
    private static final long serialVersionUID = 8997876516396600511L;

    /** 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss */
    private String timeExpire;

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("timeExpire", timeExpire)
                .toString() + super.toString();
    }
}
