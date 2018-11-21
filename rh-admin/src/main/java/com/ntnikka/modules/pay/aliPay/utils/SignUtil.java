package com.ntnikka.modules.pay.aliPay.utils;

/**
 * Created by liuq on 2018/9/11.
 */
public class SignUtil {
    /**
     * 校验MD5签名
     *
     * @param sign
     * @param ParamStr
     * @return
     */
    public static Boolean checkSign(String sign, String ParamStr) {

        String checkSign = MD5Utils.encode(ParamStr).toUpperCase();
        if (checkSign == sign || checkSign.equals(sign))
            return true;
        return false;
    }

    public static String doubleTrans(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    public static void main(String[] args) {
        String sign = "5277E5BBD3C53EAEDC0636F828B0F0C1";
        String paramStr = "bank_type=COMM_DEBIT&inst_id=20000004&out_trade_no=36951511011628006717&result_code=SUCCESS&return_code=SUCCESS&return_msg=OK&sign_type=MD5&time_end=20181101162813&total_fee=100&transaction_id=4200000196201811016833990838&key=XX35C1ABCA863318E7D530DDDA2F9EDE";
        if (checkSign(sign, paramStr)) {
            System.out.println("验签通过");
        } else {
            System.out.println("验签失败");
        }
    }
}
