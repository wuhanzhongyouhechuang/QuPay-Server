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
import com.ntnikka.modules.pay.aliPay.utils.BalanceUtil;
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
        request.setNotifyUrl("http://152.32.161.135/api/v1/AliNotify");
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

    public static String doSettleAliRequest(String appId , String privateKey , String aliPubKey , String trade_no ,BigDecimal orderAmount , String pid , String trans_in_id , String authToken , Double amountPercent) throws AlipayApiException{
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appId,privateKey,"json",AlipayConfig.input_charset,aliPubKey,"RSA2");
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        String out_request_no = IdWorker.getSysTradeNumShort();
        request.setBizContent("{" +
                "\"out_request_no\":" + out_request_no + "," +
                "\"trade_no\": "+ trade_no +"," +
                "\"royalty_parameters\":[{" +
                "\"trans_out\":" + pid + "," +
                "\"trans_in\":" + trans_in_id + "," +
                "\"amount\": " + BalanceUtil.round(BalanceUtil.mul(orderAmount.doubleValue(),amountPercent),2) + "," +
                "\"desc\":\"分账\"" +
                "}]" +
                "  }");
        AlipayTradeOrderSettleResponse response = alipayClient.execute(request,"", authToken);
        logger.info("支付宝分账返回结果 ， {}" ,response.getBody());
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

    public static void doPost(HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse) throws ServletException, IOException {
        String appid = "2018112862332988";
        String prikey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDImrF8jRarE3KQ\n" +
                "I+fDv8KhCo1ZCV4qnUom0HEP4kDRuPtqD0gsIDz6HyhQZwCY5xORZcALWKvbpGZD\n" +
                "OqrOHUgAvXUgq2l+1UIqSa29Cd/gdCvQtZlKOByXmXJkNYLByvj2ZZE6xeojWVjk\n" +
                "LXB4wc1vd2eRV4yJqV/2c8zj2EUES/2W1swgjHn9s9K6nrzZXSRODK49jMIjaUwj\n" +
                "4d92WeNfJDQ/NCF6kqsrPNQH3JgxJ1ipnqW43U4JNgeceb2Z9JrvMQVFZsPs8LrN\n" +
                "6jlnt7npwwpsqG/tpRvfRkcMhuZbrAw4mFZSw6Trxp11yUje9oD4kOfV8hjsUet6\n" +
                "ImHsNBMDAgMBAAECggEBAIUjZjiryyevuJFBw0h/tMT9I+K7/Ot7iQNJxEAugaav\n" +
                "hu1VRylCHDZFB5SVSCgy9SHCzjq5lxYnfleQUFyLwoX7OAl+un2bPFakn9KfAx8M\n" +
                "+yeB30MLCq7zAKDRgv7rn2Co+EJxqHvdiMmn/sWmmpooPWyRFo0nBLGlGY7+so+u\n" +
                "UrmdOi2L1ZMWsOFt6disX86B9WkBIUeS+Es+avQbJwwP+A3Wq0+WfOdzimV8KWo7\n" +
                "x5SeNyOQBLR9BCSUlpOWBrZxc1mrHQ1HTa1ZMYBRIbA01iMUpmd5SFSBXddS3FQz\n" +
                "Me9CH67B5W32B0PJj+5bTNpCZ/Wr4X/s9ZcE3OifF4ECgYEA5zj0fg1kIdjlGoTa\n" +
                "edPDfUvVrQtJ95bUhY0PHV2lIEkbExmcOUFvx7FfR9mkfLVCfJYdebW/Bto09TYw\n" +
                "o5+U073T5UV1Y79wpMZzQAm4+u8K9h9OhJYwddqwpWeUkjxooTTfP0+PWftNOGuv\n" +
                "5MB0VYWHyfzSwq7tb3TKx2mxADkCgYEA3hnMt8OMhbpq+Hrk61u6j9NzA8TAEgUL\n" +
                "zo6XTPtWGmLKJyO3rr4v86NkPwy0Cn7shhjWj8Y+GxXx6Ce0n+LaCzQ8J7QNHb0h\n" +
                "YZKnbl0t3bfF7pZ1ICWAI2rv1QJ//Dho0HfDXFDulSVUfx0d155fKrXacUgsPjRE\n" +
                "YyL9vh7ndRsCgYB8XqtLvgI1Hogk2n71do05MPgvCtpvXWbUqwoNPpiaJBh3Dph0\n" +
                "R5lLbUGV0SBjn8+yWcBi/fpRQj+oAczpxeV0ey+lq9W3oHqDdub49AHWn4ti1XYT\n" +
                "ZQGN8uJ/JZbJjcqto1XNpQqzWLOxfGLxBP/OHHPslpv68cJu6ABGgLgfKQKBgQCW\n" +
                "vI3bXmV90UKOd+Hr9qqMuKbDtQHiePhFbTM0tRP//nJS1APzHfMveQItZHLA+NmQ\n" +
                "PUVJ+PLnsSIoPOJy7SStG/uDush4GYeRUFZAKg9Z/kLgpc7CTZFE+GvjdrnkNWi1\n" +
                "q8K0Yd067kYFq344e6z9MMUbwy773L9ZRUNPiGvwZwKBgH1HgxTTSjuG3Fgp/f9p\n" +
                "qMd+snne2R6e/pWBne9HH5lRoq5WBMWmsAR8pD3oO3AXKoeFCpHW+ZVL9xB9ZUTk\n" +
                "4infjHppWzPlYP3mSlEObxbrQf6sNB+lcStJxS8hLYx1p6Gm8H094JsNgXLXVxkQ\n" +
                "xyPbRGy77FMie5/WFB2/bs7W";
        String aliPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhlEQ81nGElX6+/G4zu9V6y28UlKyVtPFDm31fHeU1x04RAl346XdJNWHkHQIpQz27u79nltNImYSbtNTsL3pOVw4q41u6Pj4ATr6wrYlZVEy5RMW89E6buvA+MErU/86ydPhuYZ3UbOq4CDGUo5flVxKezaipg1fqndRcwCtQ8ps448oAR6w1dmSObTT4k9MdRNR+d6hdSpXgShL641LzYctI4trGRkpqeCAI04aSunFT36pNlgLYOp5z6UEAHwuF3tZxWW2mk9GZuZrjpHytoH77gAXKCKjKB234VW6sgpp5w0LUY4P9WfMVl8UvYJweMaDZVZSvue2yGeaC3o9SQIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, prikey, "json", AlipayConfig.input_charset, aliPubKey, AlipayConfig.sign_type_RSA2); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest  alipayRequest = new AlipayTradeWapPayRequest ();//创建API对应的request
//        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        alipayRequest.setNotifyUrl("http://yxsfa7.natappfree.cc/pay-admin/api/v1/AliNotify");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+IdWorker.getSysTradeNum()+"\"," +
                "    \"product_code\":\"QUICK_WAP_PAY\"," +
                "    \"total_amount\": \"1.00\"," +
                "    \"subject\": \"网站支付\" " +
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + AlipayConfig.input_charset);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    public static void main(String[] args) {
        String jsonStr = "{\"alipay_trade_precreate_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"2015032001010100211\",\"qr_code\":\"https:\\/\\/qr.alipay.com\\/bax043173wzfnxohhdho6090\"},\"sign\":\"OUt2HdOZllr6w9txa45KSXy1fGnEhUPKOv7149nasPZdjojseYWDDQjc6pNLy2nJn9Uh4lahbGoPORtwRo26MlUWlMv+jcHGWmKbIvo2LNEYBaRSsi+nBPs3W7VF/D5MTC4pfwEuTK+dZBYuPCSuonWX4baLOVrBeFq4/k+t1UsheQom3n9ps0Xgxc17YQ8DQcMgzt+6W5zJgHqp8CkFPpKZjxf+P1UrbWgqHhNKhMg6gEscXk14dY6B90eta95PKUCWhlmUn6yn/KU4QqJF1BLkVHKHx/cq6kpIuckMeojsav0uQQT/vUfQcsaxX+8mhDR+OEPCLn9ocTpmzJeiCw==\"}";
        JSONObject json = JSON.parseObject(jsonStr).getJSONObject("alipay_trade_precreate_response");
        System.out.println(json);
    }
}
