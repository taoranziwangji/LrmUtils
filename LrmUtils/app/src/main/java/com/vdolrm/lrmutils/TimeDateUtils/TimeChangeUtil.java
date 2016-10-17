package com.vdolrm.lrmutils.TimeDateUtils;

import android.text.TextUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeChangeUtil {

    /**
     * 将毫秒数转换成时间日期 yyyy-MM-dd HH:mm:ss
     * @param millisecond
     * @return
     */
    public static String millisecondToDate(long millisecond){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millisecond);
        //String strTime = cal.getTime().toLocaleString();
        String strTime = dateToStrLong(cal.getTime());
        return strTime;
    }

    /**
     * 将长时间格式字符串转换为date时间
     *
     * @param strDate 必须为yyyy-MM-dd HH:mm:ss格式，ex:2015-10-11 11:11:11
     * @return ex:Sun Oct 11 11:11:11 GMT+08:00 2015
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将长时间Date格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间
     *
     * @param strDate 必须为yyyy-MM-dd格式，ex:2015-10-11
     * @return ex:Sun Oct 11 00:00:00 GMT+08:00 2015
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }





    /**
     * 得到二个日期间的间隔天数,需为yyyy-MM-dd格式
     */
    public static String getDaysFromTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);

            day = Math.abs(day);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }


    /**
     * 时间前推或后推分钟
     *
     * @param sj1 需要被推算的时间,yyyy-MM-dd HH:mm:ss格式
     * @param jj  推算分钟数，正数为后推，负数为前推
     * @return 新的时间
     */
    public static String getPreOrNextMinute(String sj1, String jj) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate1 = "";
        try {
            Date date1 = format.parse(sj1);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception e) {
        }
        return mydate1;
    }


    /**
     * 日期前推或后推几天
     *
     * @param nowdate 需要被推算的时间,yyyy-MM-dd格式
     * @param delay   推算天数，正数为后推，负数为前推
     * @return 新的日期
     */
    public static String getPreOrNextDay(String nowdate, String delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String mdate = "";
            Date d = strToDate(nowdate);
            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 判断是否润年
     *
     * @param ddate 必须为yyyy-MM-dd格式，ex:2015-10-11
     * @return
     */
    public static boolean isLeapYear(String ddate) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = strToDate(ddate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0) {
            if ((year % 100) == 0)
                return false;
            else
                return true;
        } else
            return false;
    }


    /**
     * 获取一个月的最后一天
     *
     * @param dat yyyy-MM-dd
     * @return
     */
    public static String getEndDateOfMonthByDate(String dat) {
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }

    /**
     * 判断二个时间是否在同一个周
     * 每周从周日开始，周六结束
     * 两个参数无先后顺序
     * 参数为yyyy-MM-dd格式或者yyyy-MM-dd HH:mm:ss格式都可以
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekFromTwoDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }


    /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     *
     * @param sdate 需要查找的日期, yyyy-MM-dd格式
     * @param num   星期几，取值0（星期日）-6（星期）
     * @return sdate日期所在的那一周中, 星期num的日期。yyyy-MM-dd格式
     */
    public static String getTheCustomDateByWeek(String sdate, String num) {
        // 再转换为时间
        Date dd = TimeChangeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if (num.equals("1")) // 返回星期一所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        else if (num.equals("2")) // 返回星期二所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        else if (num.equals("3")) // 返回星期三所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        else if (num.equals("4")) // 返回星期四所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        else if (num.equals("5")) // 返回星期五所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        else if (num.equals("6")) // 返回星期六所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        else if (num.equals("0")) // 返回星期日所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate yyyy-MM-dd格式
     * @return 星期几的数字 1=星期日 2=星期一 3=星期二 4=星期三 5=星期四 6=星期五 7=星期六，其他类推
     */
    public static int getTheWeekFromDate(String sdate) {
        // 再转换为时间
        Date date = TimeChangeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return hour;
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate yyyy-MM-dd格式
     * @return 星期几的中文，ex:星期四
     */
    public static String getTheWeekFromDateStr(String sdate) {
        Date date = TimeChangeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }


    /**
     * 返回该日历第一行星期日所在的日期
     *
     * @param sdate yyyy-MM-dd
     * @return 返回所传日期当月第一行的第一个（可能为上一个月的）星期日所在的日期 ex:2016-03-31-----2016-02-28
     */
    public static String getTheMonthFirstDay(String sdate) {
        // 取该时间所在月的一号
        sdate = sdate.substring(0, 8) + "01";
        // 得到这个月的1号是星期几
        Date date = TimeChangeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int u = c.get(Calendar.DAY_OF_WEEK);
        String newday = TimeChangeUtil.getPreOrNextDay(sdate, (1 - u) + "");
        return newday;
    }


    /**
     * 返回这个日期所在的年
     * @param date yyyy-MM-dd
     * @return
     */
    public static String getYearFromOneDate(String date){
        if(TextUtils.isEmpty(date))
            return "";
        if(date.contains("-")){
            String[] split = date.split("-");
            if(split!=null && split.length>0){
                return split[0];
            }
        }
        return "";
    }

    /**
     * 返回这个日期所在的月
     * @param date yyyy-MM-dd
     * @return
     */
    public static String getMonthFromOneDate(String date){
        if(TextUtils.isEmpty(date))
            return "";
        if(date.contains("-")){
            String[] split = date.split("-");
            if(split!=null && split.length>1){
                return split[1];
            }
        }
        return "";
    }

    /**
     * 返回这个日期所在的日
     * @param date yyyy-MM-dd
     * @return
     */
    public static String getDayFromOneDate(String date){
        if(TextUtils.isEmpty(date))
            return "";
        if(date.contains("-")){
            String[] split = date.split("-");
            if(split!=null && split.length>2){
                return split[2];
            }
        }
        return "";
    }


}