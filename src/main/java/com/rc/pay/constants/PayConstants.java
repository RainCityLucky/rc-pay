package com.rc.pay.constants;

/**
 * @Author: RainCity
 * @Date: 2022-07-27 13:41:12
 * @Desc: 支付常量类
 */
public class PayConstants {
    public static final String NUM_LETTER = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**编码*/
    public static final String UTF_8 = "UTF-8";
    /**格式*/
    public static final String FORMAT_JSON = "json";

    /**支付方式*/
    public static final String PAW_WAY = "paw_way";

    /**支付宝-扫码付*/
    public static final String SCAN_CODE = "scan_code";
    /**支付宝-当面付条码支付场景*/
    public static final String BAR_CODE = "bar_code";
    /**支付宝-手机网站支付*/
    public static final String WAP = "wap";
    /**支付宝-PC场景下单并支付*/
    public static final String PAGE = "page";

    /**微信-JSAPI(公众号支付、小程序)支付*/
    public static final String JSAPI = "JSAPI";
    /**微信-Native支付*/
    public static final String NATIVE = "NATIVE";
    /**微信-H5支付*/
    public static final String H5 = "H5";

    /**app支付*/
    public static final String APP = "APP";

    /**订单查询*/
    public static final String QUERY = "QUERY";
    /**订单撤销*/
    public static final String CANCEL = "CANCEL";
    /**订单关闭*/
    public static final String CLOSE = "CLOSE";

    /**商户证书序列号*/
    public static final String WECHAT_PAY_SERIAL = "Wechatpay-Serial";
    /**应答时间戳*/
    public static final String WECHAT_PAY_TIMESTAMP = "Wechatpay-Timestamp";
    /**应答随机串*/
    public static final String WECHAT_PAY_NONCE = "Wechatpay-Nonce";
    /**应答签名*/
    public static final String WECHAT_PAY_SIGNATURE = "Wechatpay-Signature";

    /**支付宝支付回调成功返回*/
    public static final String ALI_SUCCESS_RETURN = "success";
    /**支付宝支支付回调失败返回*/
    public static final String ALI_FAILED_RETURN = "failure";

    /**微信支付回调-通知数据类型*/
    public static final String RESOURCE_TYPE = "encrypt-resource";
    /**微信支付回调-通知类型*/
    public static final String EVENT_TYPE = "TRANSACTION.SUCCESS";

    /**微信支付回调成功返回*/
    public static final String WECHAT_SUCCESS_RETURN = "{\"code\": \"SUCCESS\",\"message\": \"支付成功\"}";
    /**微信支付回调失败返回*/
    public static final String WECHAT_FAILED_RETURN = "{\"code\": \"ERROR\",\"message\": \"支付失败\"}";
    /**微信支付回调失败返回*/
    public static final String WECHAT_NO_MSG_RETURN = "{\"code\": \"ERROR\",\"message\": \"未查询到信息\"}";

}
