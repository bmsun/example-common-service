package com.example.commons.utils;

import org.joda.time.DateTime;
import org.junit.Test;

public class DateDealUtilsTest {


    @Test
    public void testMonthDiff(){
        DateTime dt3 = new DateTime(2018, 4, 8, 13, 14, 0, 0);
        DateTime dt3_1 = new DateTime(2018, 5, 6,13,14);
        int monthDiff = DateDealUtils.getInstance().getMonthDiff(null, dt3_1.toDate());
        int days = DateDealUtils.getInstance().getDaysDiff(dt3.toDate(), dt3_1.toDate());
        System.out.println(monthDiff);
        System.out.println(days);

    }
    @Test
    public void testsecToStr(){
        System.out.println(DateDealUtils.getInstance().secToString(3710));

    }

}
