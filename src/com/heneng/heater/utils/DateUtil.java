package com.heneng.heater.utils;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO 格式化日期相关，部分从网络一位叫DDS的大神那里摘取，部分自己添加
 *
 * @author HuangYiHao
 * @data: 2014年10月25日 上午9:31:20
 * @version: V1.0
 * //                              _oo0oo_
 * //                             o8888888o
 * //                             88" . "88
 * //                             (| -_- |)
 * //                             0\  =  /0
 * //                           ___/'---'\___
 * //                        .' \\\|     |// '.
 * //                       / \\\|||  :  |||// \\
 * //                      / _ ||||| -:- |||||- \\
 * //                      | |  \\\\  -  /// |   |
 * //                      | \_|  ''\---/''  |_/ |
 * //                      \  .-\__  '-'  __/-.  /
 * //                    ___'. .'  /--.--\  '. .'___
 * //                 ."" '<  '.___\_<|>_/___.' >'  "".
 * //                | | : '-  \'.;'\ _ /';.'/ - ' : | |
 * //                \  \ '_.   \_ __\ /__ _/   .-' /  /
 * //            ====='-.____'.___ \_____/___.-'____.-'=====
 * //                              '=---='
 * //
 * //          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * //
 * //                  佛祖保佑                 永无BUG       永不修改
 * //
 */
@SuppressLint("SimpleDateFormat")
public final class DateUtil implements Serializable {

    private static final long serialVersionUID = -3098985139095632110L;

    private DateUtil() {
    }


    /**
     * 格式化日期字符串
     *
     * @param sdate  日期字符串
     * @param format 格式
     * @return
     */
    public static String dateFormat(String sdate, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        SimpleDateFormat sFormatter = new SimpleDateFormat(Constants.FORMAT_SEVER_TIME);
        Date date = sFormatter.parse(sdate);
        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 返回日期间隔天数
     *
     * @param sd 起始日期
     * @param ed 结束日期
     * @return
     */
    public static long getIntervalDays(String sd, String ed) {
        return ((java.sql.Date.valueOf(ed)).getTime() - (java.sql.Date
                .valueOf(sd)).getTime()) / (3600 * 24 * 1000);
    }

    /**
     * 转换指定格式的日期成星期
     *
     * @param strDate
     * @param partten
     * @return
     */
    public static String parseStringDateToWeekDate(String strDate,
                                                   String partten) {
        Date date = parseStringToDate(partten, strDate);
        SimpleDateFormat format = new SimpleDateFormat("E");
        return format.format(date);
    }

    /**
     * 转换指定格式的日期成long值
     *
     * @param strDate
     * @param partten
     * @return
     */
    public static long parseStringToLong(String partten, String date) {
        SimpleDateFormat format = new SimpleDateFormat(partten);
        long time = System.currentTimeMillis();

        ParsePosition pos = new ParsePosition(0);
        Date dateParse = format.parse(date, pos);
        time = dateParse.getTime();
        return time;
    }

    /**
     * 转换指定格式的日期成日期
     *
     * @param strDate
     * @param partten
     * @return
     */
    public static Date parseStringToDate(String partten, String date) {
        SimpleDateFormat format = new SimpleDateFormat(partten);

        ParsePosition pos = new ParsePosition(0);
        Date dateParse = format.parse(date, pos);

        return dateParse;
    }

    /**
     * 获取时间间隔，距离现在多少多少前
     *
     * @param st
     * @return
     */
    public static String getIntervalTimeFromNow(long st) {
        String result = "1分钟前";

        long deley = new Date().getTime() - st;

        long day = 3600 * 24 * 1000;
//        LogUtils.e(new Date().getTime() + "--" + st + "--" + deley + "---" +
//                day * 365);


        if (deley > day * 365) {
            result = (deley / (day * 365)) + "年前";
        } else if (deley > day * 30) {
            result =  (deley / (day * 30)) + "个月前";
        } else if (deley > day) {
            result = (deley / day) + "天前";
        } else if (deley > (3600 * 1000)) {
            result = ( deley / (3600 * 1000)) + "小时前";
        } else {
            result = ( deley / (60 * 1000)) + "分钟前";
        }

        return result;
    }

    /**
     * 返回间隔月份
     *
     * @param beginMonth
     * @param endMonth
     * @return
     */
    public static int getInterval(String beginMonth, String endMonth) {
        int intBeginYear = Integer.parseInt(beginMonth.substring(0, 4));
        int intBeginMonth = Integer.parseInt(beginMonth.substring(beginMonth
                .indexOf("-") + 1));
        int intEndYear = Integer.parseInt(endMonth.substring(0, 4));
        int intEndMonth = Integer.parseInt(endMonth.substring(endMonth
                .indexOf("-") + 1));

        return ((intEndYear - intBeginYear) * 12)
                + (intEndMonth - intBeginMonth) + 1;
    }

    /**
     * 返回格式化 日期
     *
     * @param sDate
     * @param dateFormat
     * @return
     */
    public static Date getDate(String sDate, String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        ParsePosition pos = new ParsePosition(0);

        return fmt.parse(sDate, pos);
    }

    /**
     * 返回当前日期 无时间
     *
     * @return
     */
    public static String getCurrentDate() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd");
    }

    /**
     * 返回当前日期+时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getFormatDate(Date date) {
        return getFormatDateTime(date, "yyyy-MM-dd");
    }

    /**
     * 格式化当前日期
     *
     * @param format 格式串
     * @return
     */
    public static String getFormatDate(String format) {
        return getFormatDateTime(new Date(), format);
    }

    /**
     * 格式化
     *
     * @param date
     * @param format
     * @return
     */
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}