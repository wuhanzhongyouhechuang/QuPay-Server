package com.ntnikka.common.utils;

/*
 * Copyright (C), 2002-2013, 苏宁易购电子商务有限公司
 * FileName: RSAUtil.java
 * Author:   王子银
 * Date:     2013-9-30 下午02:25:56
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * RSA工具类<br>
 *
 * @author 王子银 2013-9-30 下午02:25:56
 * @since 10.15
 */
public class RSAUtil {

    /**
     * RSA签名方式
     */
    private static final String RSA = "RSA";

    /**
     * RSA加密，长度支持512-2048
     */
    private static final int RSA_SIZE_2048 = 2048;

    /**
     * RSA加密，长度支持512-2048
     */
    private static final int RSA_SIZE_1024 = 1024;

    /**
     * Md5 RSA算法
     */
    @SuppressWarnings("unused")
    private static final String MD5_WITH_RSA = "MD5withRSA";

    /**
     * SHA1 RSA算法
     */
    private static final String SHA1_WITH_RSA = "SHA1WithRSA";

    /**
     * 公钥对象KEY
     */
    public static final String PUBLIC_KEY = "publicKey";

    /**
     * Base64编码后的公钥对象KEY
     */
    public static final String PUBLIC_KEY_B64 = "publicKeyBase64";

    /**
     * 私钥对象KEY
     */
    public static final String PRIVATE_KEY = "privateKey";

    /**
     * Base64编码后的私钥对象KEY
     */
    public static final String PRIVATE_KEY_B64 = "privateKeyBase64";

    /**
     * 生成1024位RSA密钥对
     *
     * @return 公私钥对象和Base64编码后的公私钥String字符串
     * @throws NoSuchAlgorithmException
     * @since 10.15
     */
    public static Map<String, Object> create1024Key() throws NoSuchAlgorithmException {
        return createKey(RSA_SIZE_1024);
    }

    /**
     * 生成2048位RSA密钥对
     *
     * @return 公私钥对象和Base64编码后的公私钥String字符串
     * @throws NoSuchAlgorithmException
     * @since 10.15
     */
    public static Map<String, Object> create2048Key() throws NoSuchAlgorithmException {
        return createKey(RSA_SIZE_2048);
    }

    /**
     * 生成公私钥对
     *
     * @param keySize 公私钥对大小
     * @return 公私钥对象和Base64编码后的公私钥String字符串
     * @throws NoSuchAlgorithmException
     * @since 10.15
     */
    public static Map<String, Object> createKey(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = null;
        keyGen = KeyPairGenerator.getInstance(RSA);
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair key = keyGen.generateKeyPair();
        PublicKey pubKey = key.getPublic();
        PrivateKey priKey = key.getPrivate();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PUBLIC_KEY, pubKey);
        map.put(PRIVATE_KEY, priKey);
        map.put(PUBLIC_KEY_B64, Base64.encodeBase64String(pubKey.getEncoded()));
        map.put(PRIVATE_KEY_B64, Base64.encodeBase64String(priKey.getEncoded()));
        return map;
    }

    /**
     * 生成公私钥文件
     *
     * @param publicFilePath  存储公钥的路径和文件名,例如：D:/pub/public.key
     * @param privateFilePath 存储私钥的路径和文件名,例如：D:/pri/private.key
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void createKey(String publicFilePath, String privateFilePath, int keySize) throws Exception {
        // 生成密钥对
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA);
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        // 将已编码的密钥（公钥和私钥）字节写到文件中
        write(publicFilePath, pair.getPublic());
        write(privateFilePath, pair.getPrivate());
    }

    /**
     * 功能描述: <br>
     * 写入一个对象
     *
     * @param path 路径
     * @param key  密钥对象
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private static void write(String path, Object key) throws Exception {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            boolean creat = file.getParentFile().mkdirs();// 创建文件目录
            if (!creat) {
                System.out.println("创建文件目录异常！");
                return;
            }
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(key);
        } catch (Exception e) {
            throw new Exception("密钥写入异常", e);
        } finally {
            if (null != oos) {
                try {
                    oos.close();
                } catch (IOException e) {
                    oos = null;
                }
            }
        }
    }

    /**
     * 验证签名 <br>
     *
     * @param data c
     * @param sign 签名值
     * @param pubk 公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @since 10.15
     */
    public static boolean vertiy(byte[] data, byte[] sign, PublicKey pubk) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(SHA1_WITH_RSA);
        signature.initVerify(pubk);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 验证签名 <br>
     *
     * @param data 原文
     * @param sign Base64编码后的签名值
     * @param pubk 公钥
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @since 10.15
     */
    public static boolean vertiy(String data, String sign, PublicKey pubk) throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException {
        return vertiy(data.getBytes(), Base64.decodeBase64(sign), pubk);
    }

    /**
     * RSA签名:
     *
     * @param data 请求原文
     * @param prik 私钥
     * @return 签名值
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @since 10.15
     */
    public static byte[] sign(byte[] data, PrivateKey prik) throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {
        Signature signature = Signature.getInstance(SHA1_WITH_RSA);
        signature.initSign(prik);
        signature.update(data);
        return signature.sign();
    }

    public static byte[] signp(byte[] data, PublicKey prik) throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {
        Signature signature = Signature.getInstance(SHA1_WITH_RSA);
        signature.initVerify(prik);
        signature.update(data);
        return signature.sign();
    }

    /**
     * RSA签名
     *
     * @param data 请求原文
     * @param prik 私钥
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String sign(String data, PrivateKey prik) throws InvalidKeyException, NoSuchAlgorithmException,
            SignatureException {
        return Base64.encodeBase64String(sign(data.getBytes(), prik)).trim();
    }

    public static String signp(String data, PublicKey prik) throws InvalidKeyException, NoSuchAlgorithmException,
            SignatureException {
        return Base64.encodeBase64String(signp(data.getBytes(), prik)).trim();
    }

    /**
     * 将通过Base64编码后的String类型的公钥字符串转换为PublicKey对象
     *
     * @param strPubKey Base64编码后的String类型的公钥字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @since 10.15
     */
    public static PublicKey getPublicKey(String strPubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(strPubKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        return pubKey;
    }

    /**
     * 将通过Base64编码后的String类型的私钥字符串转换为PrivateKey对象
     *
     * @param strPriKey Base64编码后的String类型的私钥字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @since 10.15
     */
    public static PrivateKey getPrivateKey(String strPriKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(strPriKey));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);
        return priKey;
    }

    /**
     * 功能描述: <br>
     * 根据公钥文件存放位置解析为公钥对象
     *
     * @param path 存放位置，例如：D:/pub/public.key
     * @return
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static PublicKey resolvePublicKey(String path) throws Exception {
        PublicKey pubkey = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = FileUtils.openInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            pubkey = (PublicKey) ois.readObject();
            return pubkey;
        } catch (Exception e) {
            throw new Exception("解析异常", e);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * 根据私钥文件存放位置解析为私钥对象
     *
     * @param path 存放位置，例如：D:/pri/private.key
     * @return
     * @throws Exception
     * @since 10.15
     */
    public static PrivateKey resolvePrivateKey(String path) throws Exception {
        PrivateKey prikey = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = FileUtils.openInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            prikey = (PrivateKey) ois.readObject();
            return prikey;
        } catch (Exception e) {
            throw new Exception("解析异常", e);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * 将传入的公钥对象转换为经过Base64编码后的公钥字符串
     *
     * @param pubKey
     * @return
     * @since 10.15
     */
    public static String getBase64PublicKeyString(PublicKey pubKey) {
        return Base64.encodeBase64String(pubKey.getEncoded()).trim();
    }

    /**
     * 将传入的私钥对象转换为经过Base64编码后的私钥字符串
     *
     * @param priKey
     * @return
     * @see [相关类/方法](可选)
     * @since 10.15
     */
    public static String getBase64PrivateKeyString(PrivateKey priKey) {
        return Base64.encodeBase64String(priKey.getEncoded()).trim();
    }

    /**
     * 将传入的公钥存储路径读取公钥后转换为经过Base64编码后的公钥字符串
     *
     * @param path 存放位置，例如：D:/pub/public.key
     * @return
     * @throws Exception
     * @since 10.15
     */
    public static String getBase64PublicKeyString(String path) throws Exception {
        PublicKey pubKey = resolvePublicKey(path);
        return getBase64PublicKeyString(pubKey);
    }

    /**
     * 将传入的私钥存储路径读取私钥后转换为经过Base64编码后的私钥字符串
     *
     * @param path 存放位置，例如：D:/pri/private.key
     * @return
     * @throws Exception
     * @since 10.15
     */
    public static String getBase64PrivateKeyString(String path) throws Exception {
        PrivateKey priKey = resolvePrivateKey(path);
        return getBase64PrivateKeyString(priKey);
    }

    /**
     * RSA加密<br>
     * 注意：此方法支持公钥加密私钥解密或者私钥加密公钥解密
     *
     * @param key     公钥或者私钥
     * @param message 准备加密的原文
     * @return 加密后的密文
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(Key key, String message) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] miwen = cipher.doFinal(message.getBytes());
        return String.valueOf(new BigInteger(miwen));
    }

    /**
     * RSA解密<br>
     * 注意：此方法支持公钥加密私钥解密或者私钥加密公钥解密
     *
     * @param key     私钥或者公钥
     * @param message 已加密的密文
     * @return 解密后的原文
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decrypt(Key key, String message) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] miwen = new BigInteger(message).toByteArray();
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(miwen);
        return new String(result);
    }

    public static void main(String[] args) throws Exception {
        createKey("E:/opt/qf_public.pem", "E:/opt/qf_private.pem", 1024);
        PublicKey pubKey = resolvePublicKey("E:/opt/qf_public.pem");
        PrivateKey priKey = resolvePrivateKey("E:/opt/qf_private.pem");
        System.out.println("pubKey:" + Base64.encodeBase64String(pubKey.getEncoded()));
        System.out.println("priKey:" + Base64.encodeBase64String(priKey.getEncoded()));
//        System.out.println("公钥：" + Base64.encodeBase64String(pubKey.getEncoded()));
//        System.out.println("私钥：" + Base64.encodeBase64String(priKey.getEncoded()));
//        String data = "B64DC35297E509D8078FDD64DDBBED73";
//        String signData = sign(data, priKey);
//        System.out.println("签名值为：" + signData);
//        boolean result = vertiy(data, signData, pubKey);
//        System.out.println("验签结果为：" + result);
        // String priKeyStr = getBase64PrivateKeyString("D:/RSA/private.key");
        // System.out.println(priKeyStr.length());
        // System.out.println(priKeyStr);
        // System.out.println(priKeyStr.length());
        // String pubKeyStr = getBase64PublicKeyString("D:/RSA/public.key");
        // System.out.println(pubKeyStr.length());
        // System.out.println(pubKeyStr);
        // System.out.println(pubKeyStr.length());
        // String priKeyStr =
        // "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCtKPE/xeHXpTbM/Ucs0bL56+WzeDltE4y1TTvAKtaTXIQKghmlJqtib3tbY5hy1qPG8JEUK29B0BO2isWvfkWLpeUFFOhBY0bzTc3vulpeTJMJX2xNgp4VFIZEutEPlZlP5f86Q/cyonOL4G1EXr4zfdav3Z+V5KlAk7yLlrEc9tJ8CSGsoI2agKnjZNkjQk4Dsj+gybGfyXICr8Ld1enhC2jsNLa+uONaVtXTqh0aziPT9oerqWN7DbkU4/R3TICmrCMPrlK/jRsA/mIXNivmHz1wUgs5XFE2gRIRCmRjI+PBKMZDlZpTOhSB1LG9BftfMjMULPgRor6R2kdR02qVAgMBAAECggEAc/T58TnjbhRuKUHnGuVmlCRbkx6MXjX8QSDyRVKqlM4cgijzYXwOwwFiuyVr3mNME5nfnYEkpQ7hVJmDnmzRx52VSxzgF4GS7rWIlePWRlXDFUC4dwNkNzuiWn48J9KHc1DS5MLE1DyjYeUtZt6HQlYfoW+pQah5iwwj89o6yuiGmvFGX2PaBgNmR8xyh+/ifMiOnlT3YSOi9KfJ+4PylD6gjJOJxl1pYdhUGufrjknUWmk2s+Ms/5Z5uONvbwcYItXYLzj5ltRLSOvJkD0CuforRIT1FxnZJj2Q+/wlSXt0YOZUp0DVxfFlfV582Z+svT/yRD7JSxJRG8CqX6eItQKBgQDt+H3fIU7B88ie8G6KYRhQ3nMozd6AoddZzAjoM1ACVeyzQejckILUR8WkGXV3ZjIxiH2shpIR9ubk56t45mezLPx70uhtKiRAIWnQ6E605XwgPRijIBgXl0JeV+dNe1Byke5/ATQi6bsFITn+cbQAlCxI33xBbe7xVhS+8pxmIwKBgQC6R22Ysja3LverUC75kxUHH0RmnEylz+YK730aPW6B0VsaABENTA1zKwh7kubTCiT4M9C4ZWoBEQ4GPxM09uskb8CcM1551yZnJeMUIzeMbYIG5VmnliSpDuqpkH8lmHwaXhHT9qPdfOb7LbFNdswtLK2Llz9BWldH5g361w1L5wKBgFFL5Ax3P4Wo9++8abcihoESDbtx4UKGh6BowN46sWmq2ws+SAQZ5mgJAO8Xt3aL1jfvzidDbeFvPOAUeRL7e4/NnY/btIWesPWrtI+m3USVZXtKBAGudCyTAxxh4WYp64jIa/a9fYcz3C1bdYmEZJ8R/7MBjlx35vpRkxLMpTuLAoGAeUBzS1h9myF8X0uzOAsQPfGRUYVdsvSvleCruv/PxbqjpTnz34bonlWdkOpfS3Gn/NOlgdGpfYY/bbty4h/zkWUYp9QcJAzy12Nm+z2tM8A4SgrSH+7wcAbgon0G9k7aQUOIMUOzCEH8C/WDGpxAbQtO77UEnKKUe5147hU7d3MCgYBXzepBksqs06uAS/LhkVDJgnU6+7mjiupA2e4LlXQSlXBzMv26eKBT+XhkjPXhRG3Oku4rruoRoVIbeDML+1LmHCiNS+PDiONEnFx2MNEVk+Mp11Hbs9vVKVPECcoGJhupQv6JGBtHofjGQjR4HtBzhBWBumUcubQfq+kdpHFFBQ==";
        // String pubKeyStr =
        // "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArSjxP8Xh16U2zP1HLNGy+evls3g5bROMtU07wCrWk1yECoIZpSarYm97W2OYctajxvCRFCtvQdATtorFr35Fi6XlBRToQWNG803N77paXkyTCV9sTYKeFRSGRLrRD5WZT+X/OkP3MqJzi+BtRF6+M33Wr92fleSpQJO8i5axHPbSfAkhrKCNmoCp42TZI0JOA7I/oMmxn8lyAq/C3dXp4Qto7DS2vrjjWlbV06odGs4j0/aHq6ljew25FOP0d0yApqwjD65Sv40bAP5iFzYr5h89cFILOVxRNoESEQpkYyPjwSjGQ5WaUzoUgdSxvQX7XzIzFCz4EaK+kdpHUdNqlQIDAQAB";
        // PublicKey pubKey = resolvePublicKey("D:/RSA/public.key");
        // PrivateKey priKey = resolvePrivateKey("D:/RSA/private.key");
        // // String data = "b64dc35297e509d8078fdd64ddbbed73";
        // // String signData = sign(data, priKey);
        // // System.out.println("签名值为：" + signData);
        // // boolean result = vertiy(data, signData, pubKey);
        // // System.out.println("验签结果为：" + result);
        //
        // // 加密
        // String message = "就是一个中国人而已";
        // Cipher cipher = Cipher.getInstance("RSA");
        // cipher.init(Cipher.ENCRYPT_MODE, priKey);
        // byte[] miwen = ciph
        // er.doFinal(message.getBytes());
        // String miwenstr = new BigInteger(miwen).toString();
        // System.out.println(miwenstr);
        // System.out.println("*****************");
        //
        // // 解密
        // miwen = new BigInteger(miwenstr).toByteArray();
        // // 以解密模式初始化密钥
        // Cipher cipher2 = Cipher.getInstance("RSA");
        // cipher2.init(Cipher.DECRYPT_MODE, pubKey);
        // // 解密获取明文
        // byte[] result = cipher2.doFinal(miwen);
        // String r = new String(result);
        // System.out.println(r);
    }
}
