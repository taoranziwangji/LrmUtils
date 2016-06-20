package com.vdolrm.lrmutils.TimeDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2016/3/31.
 */
public class TimeNowUtil {
    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 小时:分;秒 HH:mm:ss
     */
    public static String getStringTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 得到现在时间
     *
     * @return ex:Wed Mar 30 17:30:55 GMT+08:00 2016
     */
    public static Date getNowTimeDate() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss ex:20160330 173523
     */
    public static String getNowTimeStr() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到现在小时(以24小时算)
     */
    public static String getNowHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟 0-60
     *
     * @return
     */
    public static String getNowMinute() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 得到当前时间所在的年度是第几周
     *
     * @return 年份+的几周 ，ex:201614
     */
    public static String getTheWeekThisYear() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1)
            week = "0" + week;
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    /**
     * 根据用户传入的时间表示格式，按照这个格式返回当前时间,注意字母y不能大写。
     *
     * @param sformat ex:MMddyyyy mmss
     * @return ex:03302016 3908
     */
    public static String getDateByUserRule(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
     *
     * @param k 表示是取几位随机数，可以自己定
     */
    public static String getSqlKey(int k) {
        return getDateByUserRule("yyyyMMddhhmmss") + getARandomNumber(k);
    }


    /**
     * 返回一个随机数
     *
     * @param i 随机数的位数
     * @return
     */
    public static String getARandomNumber(int i) {
        Random jjj = new Random();
        // int suiJiShu = jjj.nextInt(9);
        if (i == 0)
            return "";
        String jj = "";
        for (int k = 0; k < i; k++) {
            jj = jj + jjj.nextInt(9);
        }
        return jj;
    }

}
