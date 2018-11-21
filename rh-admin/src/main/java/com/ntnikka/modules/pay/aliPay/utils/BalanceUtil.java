package com.ntnikka.modules.pay.aliPay.utils;

import java.math.BigDecimal;

/**
 * Created by Liuq on 2018/09/27.
 */
public class BalanceUtil {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    //这个类不能实例化
    private BalanceUtil() {
    }

    /**
     * 获取小数位数
     *
     * @param v1
     * @return
     */
    public static int scale(double v1) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        return b1.scale();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param numArray 被加数组
     * @return 参数的和
     */
    public static double add(Double... numArray) {
        if (numArray == null || numArray.length == 0) {
            return 0d;
        }
        BigDecimal sum = new BigDecimal("0");
        for (Double num : numArray) {
            if (num != null) {
                sum = sum.add(new BigDecimal(Double.toString(num)));
            }
        }
        return sum.doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param numArray 被加数组
     * @return 参数的和
     */
    public static BigDecimal add(BigDecimal... numArray) {
        if (numArray == null || numArray.length == 0) {
            return new BigDecimal(0d);
        }
        BigDecimal sum = new BigDecimal(0);
        for (BigDecimal num : numArray) {
            if (num != null) {
                sum = sum.add(num);
            }
        }
        return sum;
    }

    /**
     * 提供精确的减法运算。
     *
     * @param numArray
     * @return 两个参数的差
     */
    public static double sub(double... numArray) {
        if (numArray == null || numArray.length == 0) {
            return 0d;
        }

        BigDecimal result = new BigDecimal(Double.toString(numArray[0]));
        for (int i = 1; i < numArray.length; i++) {
            result = result.subtract(new BigDecimal(Double.toString(numArray[i])));
        }
        return result.doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v == null ? 0d : v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal roundScale(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal roundScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param num   需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(BigDecimal num, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if (num == null) {
            return null;
        }
//        BigDecimal one = new BigDecimal("1");
        BigDecimal one = BigDecimal.ONE;
        return num.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 如果小于0，则返回0
     */
    public static Double replaceZero(Double num) {
        if (num == null || num.doubleValue() < 0) {
            return 0d;
        }
        return num;
    }

    /**
     * 如果小于0，则返回0
     */
    public static Double lagerZero(Double num) {
        if (num == null || num < 0) {
            return 0d;
        }
        return num;
    }

    /**
     * 返回多个数中的最大值
     */
    public static Double max(double... numArray) {
        if (numArray == null) {
            return null;
        }

        Double max = null;
        for (Double num : numArray) {
            if (max == null) {
                max = num;
            }
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * 返回多个数中的最小值
     */
    public static Double min(double... numArray) {
        if (numArray == null) {
            return null;
        }

        Double max = null;
        for (Double num : numArray) {
            if (max == null) {
                max = num;
            }
            if (num < max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * 金额对外友好展示
     *
     * @param num
     * @return
     */
    public static String prettyParseDouble(double num) {
        if (num % 1.0 == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    public static void main(String[] args) {
        Double a = 1.00D;
        Double b = 1D;
        System.out.println("a = [" + round(a, 3) + "]");
        System.out.println("b = [" + round(b, 3) + "]");
    }
}
