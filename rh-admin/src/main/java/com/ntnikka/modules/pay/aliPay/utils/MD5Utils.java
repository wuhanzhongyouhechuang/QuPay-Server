package com.ntnikka.modules.pay.aliPay.utils;

import com.ntnikka.modules.merchantManager.entity.MerchantEntity;

import java.math.BigDecimal;
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
        BigDecimal bd = new BigDecimal("0.1").setScale(2);
        BigDecimal bd2 = new BigDecimal("0.10");
        System.out.println(bd.compareTo(bd2));
    }
}
