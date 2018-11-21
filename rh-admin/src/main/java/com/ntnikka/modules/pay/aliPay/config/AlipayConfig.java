package com.ntnikka.modules.pay.aliPay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String partner = "2088231357369791";

    public static String app_id = "2018091361358374";

    // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
    public static String seller_id = partner;

    public static String account_name = "";

    public static String account = "";

    //商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public static String private_key = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQC7ijYkXmwWjYLCoTWg55bM57chIr6MS2a4dtfamWDjsgHSRkkiNyTHwMcj0Hci5vK/zUMPwCyI5elfs94CDHeq2RAdaZYUvmovQkx7ijWtW4GQ8W7fwqyt69PQ91cCIqmWxcV5WC0aP4Q7h5Gn4dRCkBl4YkigQZ5S11QU8CUtmas5Gvm4pTgKEi5ffPmit8wY41FwpXtYWBgwe5i58cqFxbhQDlEnftpqvrolmU3nKWs7pvwOw1zKxgi+1DEz6VQ9vDxqODIQPxmmlt58ki15M5ahIkv3Ygh6vzFmDkRbt4LVyzNVW4aK3NJSF3u3Bu4+Uh9pgTwKnQ8nYoLWa9YrAgMBAAECggEBAI7Q7fI8RavXECEPizuBoYFhZEBDqUBFm3AWcMLz0qkTQffTtpXr0IKd+Aj6E//sSChwNAWmn0ORcmuDGioNPIRK0M7VPYmGiG582qmp0r611LGAaqLUKZQ45xFXilcB9ovvCDckQxSeEiETWIS0oWQfQIZAsnX4Ao2rlUhsqKNT9wPKhKq6U8r6GGq59/eDhXrFNrYFvm+NyJUnHGq3AYXFf+5HYDowsfmazCbMnMm8ZRuMUPltoDYl25RGlc8/Vmp/GIjMHWeWHtmdj+7HRlPlJeLbuN2jmxu/fLiO5xVg9r7/hL61TCMle4jnQqO1BJHXD/kfZHPfIVDHiMdHdGECgYEA9XZbQkm/+YcdGwhvCqiyWAP5imT5N9+i/hECNeLL/0iORy5q88y4tsy1Ghp9z8LZmEBxifnFnFLalYDpulvM4cf+vGxhBXXrW+6GTFosyOVcjEqEUHjctW0lDVOlqSYEW4mvaxQPFsP/nc5J9PDtz95o05XuoHSauv3iOX2amI8CgYEAw5dI0FXZNTXXiHjAu13sw/dQEknB3cyk+XGRODx3nhNYSDha15IgSxTp2sxMc0FhIGUidsrJIsppNxKnQgWnlwiee2qp0ZZuYZM7tjPsG1VYek2+xWxHMV3FrVsgHnCbJaf0c3bcJh2lV5FExynwc3e2uyzjxJWPUhcc4/mNXqUCgYEApINS3ra3J/Jam4enhKlT/pGLaioKrlwAze9wX9VFjC+AzExb+lK3ai0BJCGlON4LIHPwpNz2iLMvNQxVmkV0p852+KpazGIgAFl5QSse4PZ4dL5st9KJej9QmgH6AcgtG3zISSgEhOmQJJ/iCXyAptUVxNm0OW5tVe5a9NUuDBkCgYEAvwcazp+KLTYQ8/E8C8OIYz1ArxvqhL5Cwn3w34PvMCykpKSl1LWbZLlgTZoLcOYB/qnIJ02XdKtMZ8WIJitvq0t3vgJHKbRKynIKd8HucCPC7nDQSfGe9tAnKVE+5XwFi5mmXr9P9Movmn7/NaX4ek4J4nXNX0rTV9HLHb22HSECgYEA3tmSGcV5JRCR/wDW2bX7C9nKQN14NgmuqRGfrbQcgK91QcRbDVQqxRzLum2GYdyq7w6MeM8+a0CcNEAIg8oqdNhVROR1GaibJCGaAS755Vima7qCaA3aiX2BNYT3ej6Ndrqi0IEKSzZ8+uvazP/XbDs0X+2UDg1HUURoGr8s7NQ=";

    // 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAounwJ0cIGZLbI5m8CkqJydwl8sFlM1L1gTu10Cv5GZ76kxrRmi3KmSqNbkmY/lsQHhtmVXmSVPH/4ona0p8sNvpnnxxVLC+a2nJHgmDR59/7H+B6D4qMXW3CCHO1DCrtRvokwWb7+OLSIrrteNOGX2lg3hiRRD90UE1JBNOqX4601SYoUO7c4BPKCqQ371w2BfsTAv5jVn68soMMX9MLxICfOe58AvlpVBbe8JwBIVnW82gxyfYTBSbLQOPPSCreBlZ5PiBNvRZwEehd5RQLhCX2YB0ieq/vDLvi5Okzmjjqx8h9CR687Q0WQZ4Y+7tc68XBVd5xpv4TSz6SbwImmQIDAQAB";

    public static String zebra_public_key_md5 = "e8s54m06awo6f0kisv75v02df1t6s5yj";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://106.75.132.41:89/notify/alipay";

    public static String notify_url4enterprise = "http://106.75.130.135:9001/notify/aliTransactionNofity";

    //批量转账异步通知
    public static String transfer_notify_url = "http://api2.bmkp.cn/notify/alipayBatchTrans";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://106.75.130.135/return.html";

    // 签名方式
    public static String getSign_type_RSA = "RSA";
    public static String sign_type_RSA2 = "RSA2";
    public static String sign_type_md5 = "MD5";

    // 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
    //public static String log_path = "/Users/pandeng/Documents/";

    // 字符编码格式 目前支持utf-8
    public static String input_charset = "utf-8";
    public static String input_charset_utf8 = "utf-8";

    public static String output_charset_gbk = "GBK";

    // 支付类型 ，无需修改
    public static String payment_type = "1";

    // 调用的接口名，无需修改
    public static String service = "alipay.wap.create.direct.pay.by.user";

    public static String single_trade_query = "single_trade_query";

    public static String create_direct_play = "create_direct_pay_by_user";

    public static String batch_trans = "batch_trans_notify";

    public static String batch_trans_check = "btn_status_query";

    public static String alipay_service_gateway = "https://mapi.alipay.com/gateway.do";

//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}

