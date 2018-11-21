package com.ntnikka.modules.pay.aliPay.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Liuq on 18/9/25.
 */
public class DateUtil {
    public static final String DEFAULT_PATTERN_YM = "yyyy-MM";
    public static final String DEFAULT_PATTERN_SHORT = "yyyy-MM-dd";
    public static final String DEFAULT_PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_PATTERN_MIN = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_PATTERN_KAFKA = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DEFAULT_PATTERN_LONG_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DEFAULT_PATTERN_MM = "HH:mm";

    public static Date getDateByDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Date getDateByDay(int day) {
        return getDateByDay(new Date(), day);
    }

    /**
     * 增加多少秒（从当前时间开始）
     *
     * @param second
     * @return
     */
    public static Date getDateBySecond(int second) {
        return getDateBySecond(second, new Date());
    }

    /**
     * 增加多少秒
     *
     * @param second
     * @param date
     * @return
     */
    public static Date getDateBySecond(int second, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 增加多少分钟（从当前时间开始）
     *
     * @param minute
     * @return
     */
    public static Date getDateByMinute(int minute) {
        return getDateByMinute(minute, new Date());
    }

    /**
     * 增加多少分钟
     *
     * @param minute
     * @param date
     * @return
     */
    public static Date getDateByMinute(int minute, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 增加多少小时（从当前时间开始）
     *
     * @param hour
     * @return
     */
    public static Date getDateByHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 增加多少小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date getDateByHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 本月的第一天
     *
     * @param calendar
     * @return
     */
    public static Date getFirstDayOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getBeginDay(calendar);
    }

    public static Date getBeginDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getBeginDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date getLastDayOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getEndDay(calendar);
    }

    public static String format(Date date, String format) {
        if (Objects.isNull(date) || Objects.isNull(format)) {
            return null;
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            return fmt.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String format(String format) {
        return format(new Date(), format);
    }

    public static String generateBatchNo(String format) {
        String postfix = String.format("%04d", new Random().nextInt(9999));
        return format(format) + postfix;
    }

    public static long diffNowInDay(Date date) {
        long day = (long) (diffNowInSecond(date) / (24 * 60 * 60));
        return day;
    }

    public static long diffNowInMinutes(Date date) {
        long minute = diffNowInSecond(date) / 60;
        return minute;
    }

    public static long diffNowInSecond(Date date) {
        Calendar dateOne = Calendar.getInstance(), dateTwo = Calendar.getInstance();
        dateOne.setTime(new Date());
        dateTwo.setTime(date);
        long timeOne = dateOne.getTimeInMillis();
        long timeTwo = dateTwo.getTimeInMillis();
        long second = (timeOne - timeTwo) / 1000;
        return second;
    }

    public static Date getDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(format);

        Date date = fmt.parse(dateStr);
        return date;

    }

    public static Date getDateByStr(String dateStr, String format) {
        if (dateStr == null) return null;
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            Date date = fmt.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date formatDate(Date date, String format) {
        String str = format(date, format);
        return getDateByStr(str, format);
    }

    public static long diffSec(Date beg, Date end) {
        return (end.getTime() - beg.getTime()) / 1000;
    }

    public static long diffMinute(Date beg, Date end) {
        return diffSec(beg, end) / 60;
    }

    public static long diffHour(Date beg, Date end) {
        return diffMinute(beg, end) / 60;
    }


    public static String getCurrentDateShort() {
        Calendar calendar = Calendar.getInstance();
        return formatTime(calendar.getTime(), DEFAULT_PATTERN_SHORT);
    }

    public static Date formatCurrentDate() {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(DEFAULT_PATTERN_LONG);
            String dateStr = fmt.format(new Date().getTime());
            Date date = fmt.parse(dateStr);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatTime(Date date, String pattern) {
        if (pattern == null) {
            pattern = DEFAULT_PATTERN_SHORT;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    public static Map<String, Date> getDayRange(Calendar cal) {
        Date beg = getBeginDay(cal);
        Date end = getEndDay(cal);
        Map<String, Date> map = new HashMap<>();
        map.put("beg", beg);
        map.put("end", end);
        return map;
    }

    public static Map getWeekRange(Calendar cal) {
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date beg = getBeginDay(cal);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 6);
        Date end = getEndDay(cal);
        Map<String, Date> map = new HashMap<>();
        map.put("beg", beg);
        map.put("end", end);
        return map;
    }

    public static Map getMonthRange(Calendar cal) {
        Date beg = getFirstDayOfMonth(cal);
        Date end = getLastDayOfMonth(cal);
        Map<String, Date> map = new HashMap<>();
        map.put("beg", beg);
        map.put("end", end);
        return map;
    }

    public static Map getYearRange(Calendar cal) {
        cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        Date beg = cal.getTime();
        cal.set(cal.get(Calendar.YEAR), 11, 31, 23, 59, 59);
        Date end = cal.getTime();
        Map<String, Date> map = new HashMap<>();
        map.put("beg", beg);
        map.put("end", end);
        return map;
    }

    public static String getDateOfMonthAgo(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(cal.getTime());
    }

    public static List<String> getIntervalByHour(Date now) throws ParseException {
        List<String> timeList = new LinkedList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int hour = calendar.get(Calendar.HOUR_OF_DAY) + 2;
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < hour; i++) {
            String dateStr = String.format("%s-%s-%s %s:00:00", year, month, day, i);
            timeList.add(dateStr);
        }
        return timeList;
    }


    public static int whichDayOfTheWeek() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static Map<String, Date> getRange(int type, int num, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        if (type == 1) {
            cal.set(Calendar.DAY_OF_YEAR, num);
            return getDayRange(cal);
        } else if (type == 2) {
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.setWeekDate(year, num, 1);
            return getWeekRange(cal);
        } else {
            cal.set(Calendar.MONTH, num - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            return getMonthRange(cal);
        }
    }

    /**
     * 得到本周周一
     *
     * @return
     */
    public static Date getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 得到当天凌晨
     *
     * @return
     */
    public static Date getTodayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 得到第二天凌晨
     *
     * @return
     */
    public static Date getSecondDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 得到本周周日
     *
     * @return
     */
    public static Date getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    /**
     * oti表中需要将日期格式转换为int类型数据
     *
     * @param date 2017-05-12 00:00:00
     * @return 20170512
     */
    public static int changeDateToNum(Date date) {
        if (date == null)
            return 0;
        return Integer.parseInt(format(date, "yyyyMMdd"));
    }

    public static Long changeDateToTotalNum(Date date) {
        if (date == null)
            return 0L;
        return Long.parseLong(format(date, "yyyyMMddHHmmss"));
    }


    /**
     * 获得当前时间的str格式，格式为yyyyMMddHHmmss
     *
     * @param @return 设定文件
     * @return String    返回类型
     * @Title: todayStr
     */
    public static String today2YyyyMMddHHmmss() {
        Date date = new Date();
        return format(date, "yyyyMMddHHmmss");
    }

    /**
     * 获得当前时间的str格式，格式为yyyyMMddHHmmssSSS
     *
     * @param @return 设定文件
     * @return String    返回类型
     * @Title: todayStr
     */
    public static String today2YyyyMMddHHmmssSSS() {
        Date date = new Date();
        return format(date, "yyyyMMddHHmmssSSS");
    }

    /**
     * 获得当前时间的str格式，格式为yyMMddHHmmss
     *
     * @param @return 设定文件
     * @return String    返回类型
     * @Title: todayStr
     */
    public static String today2YyMMddHHmmss() {
        Date date = new Date();
        return format(date, "yyMMddHHmmss");
    }

    /**
     * 获得当前时间的str格式，格式为yyMMddHHmmssSSS
     *
     * @param @return 设定文件
     * @return String    返回类型
     * @Title: todayStr
     */
    public static String today2YyMMddHHmmssSSS() {
        Date date = new Date();
        return format(date, "yyMMddHHmmssSSS");
    }

    /**
     * 行程单独有时间
     *
     * @param date
     * @return
     */
    public static String getFpTime(Date date) {
        String date1 = format(date, "MM-dd HH:mm");
        String date2 = getDayForWeek(date);
        return date1 + date2;
    }


    //获取指定日期是星期几
    public static String getDayForWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (day) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
            default:
                week = "周一";
                break;
        }
        return week;
    }

    /**
     * 时间是否在误差范围内
     *
     * @param time
     * @param maxDffSecond
     * @return
     */
    public static boolean timeInDiff(long time, int maxDffSecond) {
        long diffTime = (time - System.currentTimeMillis()) / 1000;
        return (diffTime > maxDffSecond || diffTime < -1 * maxDffSecond) ? false : true;
    }


    /**
     * 获取日志 前n天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDayBefore(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    /**
     * 字符串转日期
     *
     * @param dateStr
     * @return
     */
    public static Date string2Date(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String Date2Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static String dtToStr(String dt) {
        Date date = new Date(Long.parseLong(dt));
        return Date2Str(date);
    }

    public static void main(String[] args) {
        String a = "3";
        switch (a) {
            case "1":
                String aa = "1";
                break;
            case "2":
                aa = "2";
            case "3":
                aa = "3";
            case "4":
                aa = "4";
                System.out.println("args = [" + aa + "]");
                break;
        }
    }
}
