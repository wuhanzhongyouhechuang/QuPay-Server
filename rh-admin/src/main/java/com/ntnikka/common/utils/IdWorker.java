package com.ntnikka.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName IdWorker
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/27 10:23
 **/
public class IdWorker {

    private static final String prefix = "369000";

    private static final String prefix_short = "369";

    private static final String mid = "000";

    /**
     * 获取14位日期字符串
     *
     * @return
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        System.out.println("TIME:::" + dateString);
        return dateString;
    }

    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        System.out.println("TIME:::" + dateString);
        return dateString.substring(4);
    }


    /**
     * 生成系统订单
     *
     * @param
     * @return
     */
    public static String getSysTradeNum() {
        int p1 = (int) (Math.random() * 900) + 100;
        int p2 = (int) (Math.random() * 900) + 100;
        int p3 = (int) (Math.random() * 900) + 100;
        return prefix + getStringDate() + mid + p1 + p2 + p3;
    }

    /**
     * 生成系统订单(短)
     *
     * @param
     * @return
     */
    public static String getSysTradeNumShort() {
        int p1 = (int) (Math.random() * 900) + 100;
        int p2 = (int) (Math.random() * 9000) + 1000;
        return prefix_short + p1 + getStringDateShort() + p2;
    }

    public static void main(String[] args) {
        String id = getSysTradeNumShort();
        System.out.println("args = [" + id + "]");
        String id2 = getSysTradeNum();
        System.out.println("args = [" + id2 + "]");
    }


}
