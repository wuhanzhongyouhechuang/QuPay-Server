package com.ntnikka.modules.pay.aliPay.utils;

import com.ntnikka.modules.merchantManager.entity.MerchantEntity;

import java.security.MessageDigest;

/**
 * Created by liuq on 2018/9/11.
 */
public class MD5Utils {

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        String strDigest = "";
        try {
            // 此 MessageDigest 类为应用程序提供信息摘要算法的功能，必须用try,catch捕获
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] data;
            data = md5.digest(str.getBytes("utf-8"));// 转换为MD5码
            strDigest = bytesToHexString(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return strDigest;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    // 加密后解密
    public static String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    public static String creatMerchantKey(MerchantEntity merchantEntity) {
        String str = "phone=" + merchantEntity.getMerchantPhone() + "&pid" + merchantEntity.getPid() + "&storeNumber=" + merchantEntity.getStoreNumber()
                + "&authCode=" + merchantEntity.getAuthCode()+"&dt=" + System.currentTimeMillis();
        String merchantKey = encode(str).toUpperCase();
        return merchantKey;
    }

    public static void main(String[] args) {
        String s = "orderAmount=1.0&orderId=1524709290382234&partner=120180423025954421&payMethod=22&payType=QrCode&signType=MD5&version=1.0";
        String s2 = "B67EC9504492E88E3F37C23EE7F3D3EA";
        String Md5Str = encode(s);
        System.out.println("加密============================" + Md5Str);
        String Str = convertMD5(JM("98568d540134639BE4655198a36614a4"));
        System.out.println("解密===========================" + Str);
        System.out.println(SignUtil.checkSign(Md5Str, s));
        System.out.println("================>>" + Md5Str.toUpperCase());
        String dt = "1548641254195";
        String no = "01月28日10:07";
        String money = "0.23";
        String userids = "10980";
        String sign = "df29ed0cd47b654c847b0b74906a924a";
        String type = "广发银行";
        String version = "v20181101";
        String mark = "2566";
        String account = "union";
        String signkey = "123456789";
        String checkSign = encode(dt+mark+money+no+type+signkey+userids+version);
        System.out.println("================>>" + checkSign);
    }
}
