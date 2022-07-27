package com.rc.pay.constants;

/**
 * @Author: RainCity
 * @Date: 2022-06-21 11:21:00
 * @Desc: 农银直连商户平台常量类
 */
public class BridgeConstants {

    /**签名验签算法*/
    public static final String SHA1_WITH_RSA = "SHA1withRSA";

    /**缴费中心发送商户的公网源IP*/
    public static final String IP_112_65_149_27 = "112.65.149.27";
    /**缴费中心发送商户的公网源IP*/
    public static final String IP_124_74_251_16 = "124.74.251.16";

    /**测试环境接口地址*/
    public static final String CONTRACT_URL = "http://hello.enjoy.abchina.com/jf-openapiweb/bridgeContract/contract";
    /**测试环境接口地址--单笔扣款*/
    public static final String PAY_URL = "http://hello.enjoy.abchina.com/jf-openapiweb/bridgeAgent/pay";
    /**测试环境接口地址--扣款查询接口*/
    public static final String PAY_QUERY_URL = "http://hello.enjoy.abchina.com/jf-openapiweb/bridgeAgent/payQuery";

    /**授权缴费签约申请*/
    public static final String AGENT_SIGN_REQ = "AgentSignReq";
    /**授权缴费签约确认*/
    public static final String AGENT_SIGN_SUBMIT = "AgentSignSubmit";
    /**授权缴费签约重发验证码*/
    public static final String AGENT_SIGN_RESEND = "AgentSignResend";
    /**授权缴费解约*/
    public static final String AGENT_UN_SIGN = "AgentUnSign";
    /**授权缴费签约查询*/
    public static final String AGENT_SIGN_QUERY = "AgentSignQuery";
    /**授权缴费单笔扣款*/
    public static final String AGENT_PAY = "AgentPay";
    /**授权缴费扣款查询*/
    public static final String AGENT_PAY_QUERY = "AgentPayQuery";

    /**双竖杠*/
    public static final String VERTICAL_BAR = "||";

}
