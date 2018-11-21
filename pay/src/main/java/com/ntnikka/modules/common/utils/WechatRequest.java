package com.ntnikka.modules.common.utils;

import com.ntnikka.modules.pay.aliPay.utils.MD5Utils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WechatRequest
 * @Author Liuq
 * @Description todo
 * @Date 2018/10/31 10:50
 **/
public class WechatRequest {

    private static Logger logger = LoggerFactory.getLogger(WechatRequest.class);

    private static final String wxpay_Create = "http://pay.efr360.cn/Channel/WxPay/wxPayOrder.do";
    private static final String wxpay_Query = "http://pay.efr360.cn/Channel/WxPay/wxTrdQuery.do";

    /**
     * @param merchantNo  唯一商户标识
     * @param signType    加密方式 MD5 or RSA
     * @param notifyUrl   回调地址
     * @param outTradeNo  系统订单号
     * @param totalAmount 金额
     * @param payType     支付方式 wap or 二维码
     * @return
     */
    public static String doWechatOrderCreate(String merchantNo, String ip, String signType, String notifyUrl, String outTradeNo, String totalAmount, String payType, String prikey, String body) {
        Map<String, String> map = new HashMap<>();
        map.put("inst_id", merchantNo);
        map.put("total_fee", totalAmount);
        map.put("out_trade_no", outTradeNo);
        map.put("sign_type", signType);
//        map.put("device_info", "1222211111");
        map.put("spbill_create_ip", ip);
        map.put("notify_url", notifyUrl);
        map.put("body", body);
        map.put("trade_type", payType);
        //body inst_id  notify_url   out_trade_no  sign_type  spbill_create_ip    total_fee    trade_type
        String paramStr = String.format("body=%s&inst_id=%s&notify_url=%s&out_trade_no=%s&sign_type=%s&spbill_create_ip=%s&total_fee=%s&trade_type=%s&key=%s",
                body, merchantNo, notifyUrl, outTradeNo, signType, ip, totalAmount, payType, prikey);
        String sign = MD5Utils.encode(paramStr);
        map.put("sign", sign.toUpperCase());
        JSONObject json = JSONObject.fromObject(map);
//        System.out.println("请求报文:"+json.toString());
        String resultMsg = HttpClientUtil.doPost(wxpay_Create, map);
        //String resultMsg = HttpUtil.doPost(wxpay_Create , json.toString());
        logger.info("接口返回: {}", resultMsg);
        return resultMsg;
    }

    public static String doWechatOrderQuery(String merchantNum, String outTradeNo, String signType, String wechatKey) {
        Map<String, String> map = new HashMap<>();
        map.put("inst_id", merchantNum);
        map.put("out_trade_no", outTradeNo);
        map.put("sign_type", signType);
        String paramStr = String.format("inst_id=%s&out_trade_no=%s&sign_type=%s&key=%s", merchantNum, outTradeNo, signType, wechatKey);
        String sign = MD5Utils.encode(paramStr);
        map.put("sign", sign.toUpperCase());
        String resultMsg = HttpClientUtil.doPost(wxpay_Query, map);
        return resultMsg;
    }

    public String wechatSign(String paramStr, String out_pri_key) {
        //签名
        String sign = "";
        try {
            String md5 = MD5Utils.encode(paramStr);
            System.out.println("md5结果" + md5);
            System.out.println("签名" + sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
}
