package com.rc.pay.ali.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 13:47:33
 * @Desc: 付款码支付接口 实体
 */
public class AliCodePay extends AliPay {

    private static final long serialVersionUID = 4969754067832774896L;
    /**
     * 支付场景
     * bar_code：当面付条码支付场景;
     * security_code：当面付刷脸支付场景，对应的auth_code为fp开头的刷脸标识串
     */
    String scene;
    /** 支付授权码 */
    String authCode;

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = "bar_code";
        if(StringUtils.isNotEmpty(scene)){
            this.scene = scene;
        }
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("scene", scene)
                .append("authCode", authCode)
                .toString() + super.toString();
    }
}
