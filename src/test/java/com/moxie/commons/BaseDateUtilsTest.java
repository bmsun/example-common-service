package com.moxie.commons;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangyanbo on 17/2/23.
 */
@Slf4j
public class BaseDateUtilsTest {
    @Test
    public void test() {
        Date date =  BaseDateUtils.parseDate("1977-11-11", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
        Assert.assertEquals(BaseDateUtils.toDateFormat(date), "1977-11-11");
        Date date2 = BaseDateUtils.add(date, Calendar.MONTH,  -1);
        Assert.assertEquals(BaseDateUtils.toTimeFormat(date2), "1977-10-11 00:00:00");
        Assert.assertTrue(BaseDateUtils.isSameYear(date, date2));
        Date date3 = BaseDateUtils.getMonthEnd(date2);
        Assert.assertEquals(BaseDateUtils.toDateFormat(date3), "1977-10-31");
    }

    @Test
    public void testCompare() {
        Date date1 = BaseDateUtils.parseDate("2000-01-02 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date date2 = BaseDateUtils.parseDate("2000-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Assert.assertTrue(BaseDateUtils.compareDay(date1, date2) == 1);

        date1 = BaseDateUtils.parseDate("2000-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        date2 = BaseDateUtils.parseDate("2000-01-01 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Assert.assertTrue(BaseDateUtils.compareDay(date1, date2) == 0);

        date1 = BaseDateUtils.parseDate("2000-01-02 00:00:00", "yyyy-MM-dd HH:mm:ss");
        date2 = BaseDateUtils.parseDate("2000-01-01 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Assert.assertTrue(BaseDateUtils.compareDay(date1, date2) == 1);

        date1 = BaseDateUtils.parseDate("2000-02-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        date2 = BaseDateUtils.parseDate("2000-01-01 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Assert.assertTrue(BaseDateUtils.compareDay(date1, date2) == 31);
    }
}
