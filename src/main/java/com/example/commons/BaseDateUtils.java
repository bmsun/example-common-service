package com.example.commons;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


@Slf4j
public class BaseDateUtils {
    public static final TimeZone CHINA_TIMEZONE = TimeZone.getTimeZone("GMT+8");
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd", CHINA_TIMEZONE);
    private static final FastDateFormat TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", CHINA_TIMEZONE);
    private static final FastDateFormat STANDARD_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZ", CHINA_TIMEZONE);
    private static final Long secondsPerDay = 24 * 3600l;
    private static final Long millsPerDay = 24 * 3600000l;

    public static Date parseDate(String dateStr, String... patterns) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dateStr), "无法将空字符串转换为Date对象");
        Preconditions.checkArgument(patterns.length > 0, "parseDate时必须指定至少一种格式");

        try {
            return DateUtils.parseDate(dateStr, patterns);
        } catch (Throwable e) {
            throw new RuntimeException("无法将[" + dateStr + "]转换为" + Arrays.asList(patterns) + "格式的时间");
        }
    }

    public static String format(Date date) {
        Preconditions.checkArgument(date != null);
        return STANDARD_FORMAT.format(date);
    }

    public static String format(Date date, String pattern) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(pattern), "pattern不能为空");
        return DateFormatUtils.format(date, pattern, CHINA_TIMEZONE);
    }

    public static Date add(Date date, int calendarField, int amount) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        if (amount == 0) {
            return date;
        }

        switch (calendarField) {
            case Calendar.YEAR:
                return DateUtils.addYears(date, amount);
            case Calendar.MONTH:
                return DateUtils.addMonths(date, amount);
            case Calendar.DATE:
                return DateUtils.addDays(date, amount);
            case Calendar.HOUR:
                return DateUtils.addHours(date, amount);
            case Calendar.MINUTE:
                return DateUtils.addMinutes(date, amount);
            case Calendar.SECOND:
                return DateUtils.addSeconds(date, amount);
            case Calendar.MILLISECOND:
                return DateUtils.addMilliseconds(date, amount);
            default:
                throw new IllegalArgumentException("不支持的calendar类型");
        }
    }

    public static String toDateFormat(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        return DATE_FORMAT.format(date);
    }

    public static Date fromDateFormat(String s) {
        Preconditions.checkArgument(StringUtils.isNotBlank(s), "时间字串不能为空");
        try {
            return DATE_FORMAT.parse(s);
        } catch (Exception e) {
            throw new IllegalArgumentException("格式不符合yyyy-MM-dd");
        }
    }

    public static String toTimeFormat(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        return TIME_FORMAT.format(date);
    }

    public static Date fromTimeFormat(String s) {
        Preconditions.checkArgument(StringUtils.isNotBlank(s), "时间字串不能为空");
        try {
            return TIME_FORMAT.parse(s);
        } catch (Exception e) {
            throw new IllegalArgumentException("格式不符合yyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 按中国时区. e.g. 如果当前为1月1日0点, 此时在美国将会是去年12月31号, 这时返回的时间将会是1月1日,
     */
    public static Date getMonthStart() {
        return getMonthStart(new Date());
    }

    /**
     * 按中国时区.
     */
    public static Date getMonthStart(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        String chinaDate = toDateFormat(date);
        chinaDate = chinaDate.substring(0, 8) + "01";
        return fromDateFormat(chinaDate);
    }


    /**
     * 按中国时区.
     */
    public static Date getMonthEnd() {
        return getMonthEnd(new Date());
    }

    /**
     * 按中国时区.
     */
    public static Date getMonthEnd(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        Date nextMonthStart = getNextMonthStart(date);
        return DateUtils.addSeconds(nextMonthStart, -1);
    }

    /**
     * 按中国时区.
     */
    public static Date getNextMonthStart() {
        return getNextMonthStart(new Date());
    }

    public static Date getNextMonthStart(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        String chinaDate = toDateFormat(date);
        int month = Integer.valueOf(chinaDate.substring(5, 7)) + 1;
        String sMonth = month + "";
        if (month < 10) {
            sMonth = "0" + sMonth;
        }
        return fromDateFormat(chinaDate.substring(0, 5) + sMonth + "-01");
    }

    public static Date getDayStart(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        return BaseDateUtils.parseDate(BaseDateUtils.format(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    public static Date getDayEnd(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        return BaseDateUtils.parseDate(BaseDateUtils.format(date, "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
    }


    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar calendar1 = DateUtils.toCalendar(date1);
        Calendar calendar2 = DateUtils.toCalendar(date2);
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR));
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar calendar1 = DateUtils.toCalendar(date1);
        Calendar calendar2 = DateUtils.toCalendar(date2);
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) &&
                (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar calendar1 = DateUtils.toCalendar(date1);
        Calendar calendar2 = DateUtils.toCalendar(date2);
        return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfMonth(Date date) {
        Preconditions.checkArgument(date != null, "时间对象不能为空");
        return DateUtils.toCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int compareDay(Date date1, Date date2) {
        Preconditions.checkArgument(date1 != null && date2 != null, "时间对象不能为空");
        date1 = getDayStart(date1);
        date2 = getDayStart(date2);
        return (int)((date1.getTime() - date2.getTime()) / millsPerDay);
    }
}
