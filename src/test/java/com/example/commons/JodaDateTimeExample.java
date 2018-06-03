package com.example.commons;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/***
 * @Date 2018/5/5
 * @Description joda-time 日期工具案例
 * @author zhanghesheng
 * */
public class JodaDateTimeExample {

    /**
     * DateTime
     *它以毫秒级的精度封装时间上的某个瞬间时刻。DateTime 始终与 DateTimeZone 相关，如果您不指定它的话，它将被默认设置为运行代码的机器所在的时区。可以使用多种方式构造DateTime对象
     * */
    @Test
    public  void testTime() {
        DateTime dt = new DateTime();
        DateTime dt1 = DateTime.now();
        DateTime dt2 = new DateTime(new Date());
      // 指定年月日点分秒生成(参数依次是：年,月,日,时,分,秒,毫秒)
        DateTime dt3 = new DateTime(2018, 5, 6, 13, 24, 0, 0);
        DateTime dt3_1 = new DateTime(2018, 5, 6,13,14);

        // 制定ISO8601生成
        DateTime dt4 = new DateTime("2018-05-06T13:24:3");
        DateTime dt5 = new DateTime("2018-05-06");
        DateTime dt6 = new DateTime(Calendar.getInstance());
        //字符串转换为时间格式
        DateTime dt7 = DateTime.parse("2018-05-06 13:14:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(dt);
        System.out.println(dt1);
        System.out.println(dt2);
        System.out.println(dt3);
        System.out.println(dt3_1);
        System.out.println(dt4);
        System.out.println(dt5);
        System.out.println(dt6);
        System.out.println(dt7);
    }

    /**
     * DateTime
     * 获取年月日时分
     * */
    @Test
    public  void testGetDate() {
        DateTime dte = DateTime.parse("2018-02-01 13:14:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        int year = dte.getYear(); // 2018
        System.out.println(year);
        year = dte.getYearOfCentury(); // 18
        System.out.println(year);
        year = dte.getYearOfEra(); // 2018
        System.out.println(year);
        int month = dte.getMonthOfYear();//2
        System.out.println(month);
        int day = dte.getDayOfMonth();
        day = dte.getDayOfWeek(); // 星期4
        System.out.println(day);
        day = dte.getDayOfYear();// 一年的第多少天 32
        System.out.println(day);
        int hours = dte.getHourOfDay();//13
        System.out.println(hours);
        int mills = dte.getMinuteOfHour();//14
        System.out.println(mills);
        int second = dte.getSecondOfMinute();//0
        System.out.println(second);

    }


    /**
     * DateTime
     * 日期比较
     * */
    @Test
    public  void testDateCompare(){
        DateTime dateTime2 = new DateTime("2014-09-03");
        DateTime dateTime3 = new DateTime("2014-08-01");
        dateTime2.isAfter(dateTime3); // 后
        dateTime2.isBefore(dateTime3); // 前
        dateTime2.isEqual(dateTime3); // 等
        System.out.println(dateTime3.isEqual(new DateTime())); // 和系统时间比较 false
        // 和系统时间比较
        dateTime3.isAfterNow();
        dateTime3.isEqualNow();
        System.out.println(dateTime3.isBeforeNow()); // true
    }

    /**
     * DateTime
     * 日期增加减少
     * */
    @Test
    public  void testDatePlusOrMinus(){
        DateTime dtx = DateTime.now();
        DateTime dateTime1 = dtx.plusDays(1);
        System.out.println(dateTime1);
        dtx.plusMonths(1);
        dtx.plusYears(1);// 增加1 day,1 months,1 year
        dtx.minusDays(1);
        dtx.minusMonths(1);
        dtx.minusYears(1); // 减少1day，1月，1年
        DateTime dteEnd = dtx.dayOfMonth().withMaximumValue(); // 月末日期
        System.out.println(dteEnd);
        DateTime dteStart = dtx.dayOfMonth().withMinimumValue(); // 月初日期
        System.out.println(dteStart);
        DateTime dxys = dtx.plus(1000); // 增加1秒后的时间，参数为毫秒值
        System.out.println(dxys);
        DateTime dateTime = DateTime.now().withHourOfDay(12).withMinuteOfHour(00); // 当天12:00的日期
        System.out.println(dateTime);
    }


    /**
     * DateTime
     * 日期格式化字符串
     * */
    @Test
    public  void testDateFormat(){
        DateTime dateTimew = new DateTime();
        // 转成字符串
        dateTimew.toString("yyyy-MM-dd");
        dateTimew.toString("yyyy/MM/dd hh:mm:ss.SSSa");
        dateTimew.toString("yyyy-MM-dd HH:mm:ss");
        dateTimew.toString("EEEE dd MMMM, yyyy HH:mm:ssa");
        dateTimew.toString("yyyy/MM/dd HH:mm ZZZZ");
        dateTimew.toString("yyyy/MM/dd HH:mm Z");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        dateTimew.toString(formatter);
    }
}
