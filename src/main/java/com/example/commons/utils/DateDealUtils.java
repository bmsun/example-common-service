package com.example.commons.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;
/***
 * @Date 2018/3/22
 * @Description 静态内部类式单例设计模式 （线程安全的懒汉式）
 * @author zhanghesheng
 * */
public class DateDealUtils {

    //只会被加载一次，线程安全
    static class Inner {
        private  static DateDealUtils instance = new DateDealUtils();
    }

    private DateDealUtils() {

    }

    public static DateDealUtils getInstance() {
        return Inner.instance;
    }


    /**
     * 获取两个日期相差的月数
     *
     * @param maxDate 较大的日期
     * @param minDate 较小的日期
     * @return 如果maxDate>minDate返回 月数差 否则返回0
     */
    public  int getMonthDiff(Date maxDate, Date minDate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(maxDate);
        c2.setTime(minDate);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) yearInterval--;
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) monthInterval--;
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 获取两个日期相差的月数
     *
     * @param maxDate 较大的日期
     * @param minDate 较小的日期
     * @return  天数差
     *          maxDate < minDate返回负值
     */
   public int getDaysDiff(Date maxDate, Date minDate) {

            Calendar calendarMinDate = Calendar.getInstance();
            calendarMinDate.setTime(minDate);
            Calendar calendarMaxDate = Calendar.getInstance();
            calendarMaxDate.setTime(maxDate);
            int days = (calendarMaxDate.get(Calendar.YEAR) - calendarMinDate.get(Calendar.YEAR)) * 365 +
                    calendarMaxDate.get(Calendar.DAY_OF_YEAR) - calendarMinDate.get(Calendar.DAY_OF_YEAR);
            return days;

    }
    /**
     * 获取两个日期相差的月数
     *
     * @param durationInSecond 秒数值
     * @return 时分秒格式
     *
     */
    public String secToString(Integer durationInSecond) {
        String timeStr = null;
        int hour, minute, second;
        if (durationInSecond != null) {
            if (durationInSecond <= 0) {
                return "0秒";
            } else {
                minute = durationInSecond / 60;
                if (minute <= 0) {
                    timeStr = durationInSecond + "秒";
                } else if (minute > 0 && minute < 60) {
                    second = durationInSecond % 60;
                    timeStr = minute + "分" + second + "秒";
                } else {
                    hour = minute / 60;
                    minute = minute % 60;
                    second = durationInSecond - hour * 3600 - minute * 60;
                    timeStr =hour + "时"+ minute + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    //格式化 yyyy-MM
    public   String formatMonth(Date date ,String pattern) {
        return date == null ? "" : DateFormatUtils.format(date, pattern);
    }

}
