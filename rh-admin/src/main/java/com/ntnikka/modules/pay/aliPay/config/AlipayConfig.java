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

    //appid = 2018092661497515的密钥 用来生成口令红包获取userid
    //商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public static String private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKypEvW4Zb5eTdo6Hno915NzpO5umBAFEn4mEy4drURjlziFLCWU7JkHLh2rB5m43IcxssNfWwU9st8M3z2GMmySv69uSp88+osTnUmTXFGUNFA95pelPnzumpaIMtJv8BzKsMFqH8186GPzqLuv1DkQCun/OmB02ibcbewXqd7efH7h/3NsgFpruRzEOdjyANZiUBZjEIB9LruUEL6E7HAa5vrjy3CO4h5FQO3tNF13gHbG+FaQYBN0lHtSgQ7uCl21CHbPCLrjTsEACCtgcWjIRjlqfwtPJkn3pkZEKtaXIIUMMCcK3cgoLoItlGV1EZxLCguCHFeIPGk7xNIYBPAgMBAAECggEATmWr6t89Hs3WIGgcvTavlJfgpM8EWOzv6qKSGua+8CcWrq7zaHp/6ZVhfzhDdP2r8e0rDScRt982MFYUT8gMAc2ivSkW8vUYeIZbTz6Xw9TITcSorlX97TPQgvPZHPkpFEAi4joqmCLisqwmiuU9yBuxEfKh80euz7BIpG0PsQLrc369Np7d8WsHgw6B3GzC3TE4mn2gvwbFY9nUraWwRMxJpzb8LBsewE38zjM0p5k5uM2wLfsNE24ltW5FyRkIA01PQZ8v30t/vlGBYntMNnl8NLAi4PsENKbyBm98DvUdFd6d3rBM9xyrJszBKSbX9iVxn7p7X0SMggk4PX3dOQKBgQC+Rtzxs8/ZbmnG7hrcUp08YiMyFBwsn9ZUWUWJCR7230SB0fkZqzIQOApey7jzy0nkB3p7Qk18AiHyeHFsCo2fQDE4nyvVWw0QlD2jV6mOWluvv6Vth6OcrV5aj+AO5I8jcC2KBXqwOcOIEz54TdEddJvD7YUo9bqvVrDr3K1pNQKBgQC6ux7MkUE2MMX2n6BpujkBRiT449PKWtmaqHLCxJSJyExAB+qRu6WN6d+7S+AD0gFvnGVEnTTQd7Th8V1JBtZr+QWSq1j9DKj6EFJwSEZ3TF1Krz1ZPsftSgZtziFIDOtmyMvSMixpAf5wuLg0JWJaCE183QzYx/8yCFLFqQN38wKBgQCRb/6/bI5phqCpYiP71dXDasu2InLqlP2xGU8yEFuvnTZy+DirqxQoG32puZPUHMWM2z5+ak5phAPIntErIOHhIKK+wcMyYFcbHgQDDyVV3rEII7dhgfTH9CgTlrdPCtpx3vOf9NIzUuOm5faw4+H73r6Uwr8ucKzSCrROhC20JQKBgA5JPqP8APc6aArkT4uHOdFFIpMAKzXyGW/hr1YDYDHiZyMG+AVKS/I9kGZt+aeRK5b0ajMDrAS/A9G9e5uYsFL2bFy6S0ag71SiZww8G1gJOaH7IkBvszAOV8uS160BgAkPF7jvKcKm6maJW15x9cJZnEQPTWpQcs/LHzoMIj3NAoGBAL4Mz3KJPC6f7GocmWoYCOFnNQ/Yk2UBkpQSskc7jQc2//dVZZb0VnwbbE4utI+zoBOkgD3GEbFXPvfbc6aFoQJdJwdLeAFFScFnoAT2iUBwnqNcOnsLrPdQCvu2XIN1TTjM5ITDZPoD1oAiEBWzaZUGp14jdHjT9tUDBj0IyBFR";

    // 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiX+NpQ9NKbbvK3pjMM/mcRynbLulsE9A8o8SUU3DVfF9EXpLxDnXIZgvnNcaVJXMXZOqXrAn5RgL1evBCAYJz7+Q7c3JjU9ODOlSzOFP8zG2ZL7d4Kz7wKGh4M4H/fhMFvfwXjWTHMjgrtQwkRvddQ7zV5IkT693HsFJG3/HTULAVMvTiBZi/N8GlwQ9IaqPLnzuS7wAYUlucxT16AZPFdbZdhrR0MdIpaGr4RMfZfQNtSwt/owsOIV8LhSswfcicUxziDzWujYFnSZrkfBzagytvgL12INTqZTx7bWf+siYnWZiTqvpkb82HiJgyqF7d1HyF+vejKMcDzIXfYOk4wIDAQAB";

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

