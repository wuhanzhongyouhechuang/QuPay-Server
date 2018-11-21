package com.ntnikka.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @ClassName IPUtils
 * @Author Liuq
 * @Description todo
 * @Date 2018/10/31 11:21
 **/
public class IPUtils {
    public static String getServerIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostAddress = address.getHostAddress();
            return hostAddress;
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("args = [" + getServerIp() + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
