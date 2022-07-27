package com.rc.pay.web;

import com.wr.common.core.web.controller.BaseController;
import com.wr.common.core.web.domain.AjaxResult;
import com.wr.pay.remote.domain.dto.*;
import com.wr.pay.service.BridgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: RainCity
 * @Date: 2021-11-01 14:04:37
 * @Desc: 直连商户平台（BRIDGE）支付调用
 */
@RestController
@RequestMapping("/bridge")
public class BridgeController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BridgeController.class);

    @Autowired
    private BridgeService bridgeService;

    @PostMapping("/sign_req")
    public AjaxResult signReq(@RequestBody SignReqDto signReqDto) {
        log.info("********** 授权缴费签约申请===> *************");
        log.info("请求参数:{}",signReqDto.toString());
        return bridgeService.agentSignReq(signReqDto);
    }

    @PostMapping("/sign_submit")
    public AjaxResult signSubmit(@RequestBody SignSubmitDto signSubmitDto) {
        log.info("********** 授权缴费签约确认===> *************");
        log.info("请求参数:{}",signSubmitDto.toString());
        return bridgeService.agentSignSubmit(signSubmitDto);
    }

    @PostMapping("/sign_resend")
    public AjaxResult signResend(@RequestBody SignResendDto signResendDto) {
        log.info("********** 授权缴费签约重发验证码==> *************");
        log.info("请求参数:{}",signResendDto.toString());
        return bridgeService.agentSignResend(signResendDto);
    }

    @PostMapping("/un_sign")
    public AjaxResult unSign(@RequestBody UnSignDto unSignDto) {
        log.info("********** 授权缴费解约==> *************");
        log.info("请求参数:{}",unSignDto.toString());
        return bridgeService.agentUnSign(unSignDto);
    }

    @PostMapping("/sign_query")
    public AjaxResult signQuery(@RequestBody SignQueryDto signQueryDto) {
        log.info("********** 授权缴费签约查询==> *************");
        log.info("请求参数:{}",signQueryDto.toString());
        return bridgeService.agentSignQuery(signQueryDto);
    }

    @PostMapping("/agent_pay")
    public AjaxResult agentPay(@RequestBody AgentPayDto agentPayDto) {
        log.info("********** 授权缴费单笔扣款==> *************");
        log.info("请求参数:{}",agentPayDto.toString());
        return bridgeService.agentPay(agentPayDto);
    }

    @PostMapping("/pay_query")
    public AjaxResult payQuery(@RequestBody PayQueryDto payQueryDto) {
        log.info("********** 授权缴费扣款查询==> *************");
        log.info("请求参数:{}",payQueryDto.toString());
        return bridgeService.agentPayQuery(payQueryDto);
    }

}
