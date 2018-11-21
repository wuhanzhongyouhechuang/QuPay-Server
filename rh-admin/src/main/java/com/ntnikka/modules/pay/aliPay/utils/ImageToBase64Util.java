package com.ntnikka.modules.pay.aliPay.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @ClassName ImageToBase64Util
 * @Author Liuq
 * @Description 图片转base64码
 * @Date 2018/9/17 9:09
 **/
public class ImageToBase64Util {

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        //
        if (imgStr == null) //图像数据为空
            return false;

        try {
            //Base64解码
            byte[] b = Base64.decodeBase64(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param imgURL 网络资源位置
     * @return Base64字符串
     * @Title: GetImageStrFromUrl
     * @Description: TODO(将一张网络图片转化成Base64字符串)
     */
    public static String GetImageStrFromUrl(String imgURL) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            data = new byte[inStream.available()];
            inStream.read(data);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * 创建图片二维码 返回图片base64码
     *
     * @param data
     * @return
     */
    public static String createQRCode(String data) {

        ByteArrayOutputStream bos = null;

        MultiFormatWriter formatWriter = new MultiFormatWriter();

        Hashtable<EncodeHintType, Object> param = new Hashtable<EncodeHintType, Object>();
        param.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        param.put(EncodeHintType.CHARACTER_SET, "utf-8");
        param.put(EncodeHintType.MARGIN, 0);

        try {
            BitMatrix bitMatrix = formatWriter
                    .encode(data, BarcodeFormat.QR_CODE, 300, 300, param);
            // 1.1去白边
            int[] rec = bitMatrix.getEnclosingRectangle();
            int resWidth = rec[2] + 1;
            int resHeight = rec[3] + 1;
            BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
            resMatrix.clear();
            for (int i = 0; i < resWidth; i++) {
                for (int j = 0; j < resHeight; j++) {
                    if (bitMatrix.get(i + rec[0], j + rec[1])) {
                        resMatrix.set(i, j);
                    }
                }
            }
            int width1 = resMatrix.getWidth();
            int height1 = resMatrix.getHeight();
            BufferedImage qrcode = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width1; x++) {
                for (int y = 0; y < height1; y++) {
                    qrcode.setRGB(x, y, resMatrix.get(x, y) == true ? Color.BLACK.getRGB() : Color.WHITE
                            .getRGB());
                }
            }
            bos = new ByteArrayOutputStream();
            ImageIO.write(qrcode, "png", bos);

            String img = StringUtils.deleteWhitespace(Base64.encodeBase64String(bos.toByteArray()));
            return String.format("data:image/png;base64,%s", img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }

    }


    public static void main(String[] args) {
        String imgFile = "https://qr.alipay.com/bax01288y2l392an3rjh206c";//待处理的图片
        String imgBase = GetImageStrFromUrl(imgFile);
//        String imgbese=getImgStr(imgFile);
        System.out.println(imgBase.length());
        System.out.println(imgBase);
        String imgFilePath = "d:\\testPic\\123.png";//新生成的图片
        generateImage(imgBase, imgFilePath);
    }
}
