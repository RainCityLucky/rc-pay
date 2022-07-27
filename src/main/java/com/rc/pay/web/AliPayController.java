package com.rc.pay.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.rc.pay.ali.AliPayUtil;
import com.rc.pay.ali.model.AliPay;
import com.rc.pay.constants.AliConstants;
import com.rc.pay.constants.PayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: RainCity
 * @Date: 2021-11-01 14:04:37
 * @Desc: 支付宝支付调用 回调示例
 */
public class AliPayController {
    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    //@PostMapping("/ali_notify")
    public String aliNotify(HttpServletRequest request) {
        log.info("********** 支付宝支付回调执行 *************");
        //获取支付宝POST过来反馈信息
        Map<String,String> params = AliPayUtil.getPostParams(request);
        log.info("params：{}",params);
        String appId = params.get("app_id");
        log.info("异步通知==>appId:{}",appId);
        String pbp = params.get(AliConstants.PASS_BACK_PARAMS);
        JSONObject passBackParams= JSON.parseObject(pbp);
        String payWay = passBackParams.getString(PayConstants.PAW_WAY);
        log.info("异步通知==>支付方式:{}",payWay);
        // TODO: 2021/11/1 根据 appId 到数据库中查询
        AliPay aliPay = new AliPay();
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, aliPay.getPublicKey(),
                    PayConstants.UTF_8, AliConstants.SIGN_TYPE);
            if(flag){
                log.info("支付宝回调签名认证成功");
                String tradeStatus= params.get("trade_status");
                if(AliConstants.TRADE_SUCCESS.equals(tradeStatus)){
                    log.info("*******交易成功******");
                    String outTradeNo= params.get("out_trade_no");
                    // TODO: 2021/11/1 向数据库中加入支付记录
                    return PayConstants.ALI_SUCCESS_RETURN;
                }
            }
            return PayConstants.ALI_FAILED_RETURN;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("支付宝{}方式回调失败，code:{},msg:{}",payWay,e.getErrCode(),e.getErrMsg());
            return PayConstants.ALI_FAILED_RETURN;
        }
    }

}
