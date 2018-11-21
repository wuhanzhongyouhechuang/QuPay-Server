package com.ntnikka.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.ntnikka.modules.pay.aliPay.config.AlipayConfig;
import com.ntnikka.modules.pay.aliPay.contorller.AliPayController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class AliPayRequest {

    private static Logger logger = LoggerFactory.getLogger(AliPayRequest.class);


    public static String doQrCodeAliRequest(String orderId, BigDecimal orderAmount, String productName, String appId, String privateKey, String aliPubKey, String authToken, String pid, String storeId) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", AlipayConfig.input_charset, aliPubKey, AlipayConfig.sign_type_RSA2); //获得初始化的AlipayClient
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        request.setNotifyUrl("http://47.92.241.14/api/v1/AliNotify");
        if (StringUtils.isEmpty(storeId)) {//没有商户id
            request.setBizContent("{" +
                    "    \"out_trade_no\":\"" + orderId + "\"," +
                    "    \"total_amount\":\"" + orderAmount + "\"," +
                    "    \"subject\":\"" + productName + "\"," +
                    "    \"timeout_express\":\"1c\"}");
        } else {
            request.setBizContent("{" +
                    "    \"out_trade_no\":\"" + orderId + "\"," +
                    "    \"total_amount\":\"" + orderAmount + "\"," +
                    "    \"subject\":\"" + productName + "\"," +
                    "    \"store_id\":\"" + storeId + "\"," +
                    "    \"timeout_express\":\"1c\"}");//该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m
        }
        //设置业务参数alipayClient.execute(precreateRequest, appAuthToken)
        AlipayTradePrecreateResponse response = alipayClient.execute(request, "", authToken);
        System.out.print(response.getBody());
        //根据response中的结果继续业务逻辑处理
        logger.info("支付宝返回结果 ， {}", response.getBody());
        return response.getBody();
    }

    public static String getAuthToken(String appId, String privateKey, String aliPubKey, String authCode) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", AlipayConfig.input_charset, aliPubKey, AlipayConfig.sign_type_RSA2);
//        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
//        request.setGrantType("authorization_code");
//        request.setCode(authCode);
////        request.setRefreshToken("201208134b203fe6c11548bcabd8da5bb087a83b");
//        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
        request.setBizContent("{" +
                "\"grant_type\":\"authorization_code\"," +
                "\"code\":\"" + authCode + "\"" +
                "  }");
        AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
        logger.info("auth_token : {}", response.getBody());
        return response.getBody();
    }

    public static String queryOrder(String appId, String privateKey, String aliPubKey, String out_trade_no, String authCode) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", AlipayConfig.input_charset, aliPubKey, AlipayConfig.sign_type_RSA2);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + out_trade_no + "\"" +
                "  }");
        AlipayTradeQueryResponse response = alipayClient.execute(request, "", authCode);
        if (response.isSuccess()) {
            logger.info("查询订单状态成功 ：{}", response.getBody());
        } else {
            logger.info("查询订单状态失败 ：{}", response.getBody());
        }
        return response.getBody();
    }


    public static void main(String[] args) {
        String jsonStr = "{\"alipay_trade_precreate_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"2015032001010100211\",\"qr_code\":\"https:\\/\\/qr.alipay.com\\/bax043173wzfnxohhdho6090\"},\"sign\":\"OUt2HdOZllr6w9txa45KSXy1fGnEhUPKOv7149nasPZdjojseYWDDQjc6pNLy2nJn9Uh4lahbGoPORtwRo26MlUWlMv+jcHGWmKbIvo2LNEYBaRSsi+nBPs3W7VF/D5MTC4pfwEuTK+dZBYuPCSuonWX4baLOVrBeFq4/k+t1UsheQom3n9ps0Xgxc17YQ8DQcMgzt+6W5zJgHqp8CkFPpKZjxf+P1UrbWgqHhNKhMg6gEscXk14dY6B90eta95PKUCWhlmUn6yn/KU4QqJF1BLkVHKHx/cq6kpIuckMeojsav0uQQT/vUfQcsaxX+8mhDR+OEPCLn9ocTpmzJeiCw==\"}";
        JSONObject json = JSON.parseObject(jsonStr).getJSONObject("alipay_trade_precreate_response");
        System.out.println(json);
    }
}
