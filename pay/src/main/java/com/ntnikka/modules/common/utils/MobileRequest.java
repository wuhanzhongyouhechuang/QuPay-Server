package com.ntnikka.modules.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @ClassName MobileRequest
 * @Author Liuq
 * @Description todo
 * @Date 2018/10/25 14:08
 **/
public class MobileRequest {
    private static Logger logger = LoggerFactory.getLogger(MobileRequest.class);

    public static String queryOrder(String sysTradeNo, String mobileUrl) {
        String reqUrl = mobileUrl + "/getresult?trade_no=" + sysTradeNo;
        String resultMsg = HttpClientUtil.doGet(reqUrl);
        return resultMsg;
    }

    public static String createOrderAlipay(String mobileUrl, BigDecimal orderAmount, String sysTradeNo) {
        String reqUrl = String.format(mobileUrl + "/getpay?money=%s&mark=%s&type=alipay", orderAmount, sysTradeNo);
        String result = HttpClientUtil.doGet(reqUrl);
        return result;
    }

    public static String createOrderWechat(String mobileUrl, BigDecimal orderAmount, String sysTradeNo) {
        String reqUrl = String.format(mobileUrl + "/getpay?money=%s&mark=%s&type=wechat", orderAmount, sysTradeNo);
        String result = HttpClientUtil.doGet(reqUrl);
        return result;
    }

    public static String createOrderMobile(String mobileUrl, BigDecimal orderAmount, String sysTradeNo, String payType) {
        String reqUrl = String.format(mobileUrl + "/getpay?money=%s&mark=%s&type=%s", orderAmount, sysTradeNo, payType);
        String result = HttpClientUtil.doGet(reqUrl);
        return result;
    }

}
