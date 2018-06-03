package com.example.commons.utils;


import com.example.commons.model.UserEntity;
import com.thoughtworks.xstream.XStream;
import lombok.Data;

import org.joda.time.DateTime;
import org.junit.Test;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class MyTest {

    /**
     * @return
     * @author zhanghesheng
     * @description xstream方式xml与Object互转
     * @date 2017/11/19
     */
    @Test
    public void testXml() {
        XStream xs = new XStream();
        Bean1 bean1 = new Bean1();
        bean1.setF1("aaa");
        bean1.setF2("bbb");
        String toXML = xs.toXML(bean1);
        Bean1 fromXML = (Bean1) xs.fromXML(toXML);
        System.out.println(toXML);
        System.out.println(fromXML.getF1() + "\t" + fromXML.getF2());
    }

    @Test
    public void testFile() throws Exception {
        URL resource = this.getClass().getClassLoader().getResource("");
        String dir = System.getProperty("user.dir")+"/src/main/resources/";
        //File file = new File(dir+"a.txt");
       // if(file.exists()) file.delete();
        //file.createNewFile();
        System.out.println(dir);
        System.out.println(resource.getPath());

    }

    @Test
    public void testString() throws Exception {
        String s = "\\123";
        System.out.println(s.lastIndexOf("."));
        System.out.println(s.substring(0,-1));

    }

    @Test
    public void testInnerBuilder(){
        UserEntity build = new UserEntity.Builder().age(1).name("zhangsan").lastModifyTime(new Date()).build();
        System.out.println(build);
    }


    @Test
    public void testjsoup() throws Exception {
        DateTime dt3 = new DateTime(2018, 5, 8, 13, 14, 0, 0);
        DateTime dt3_1 = new DateTime(2018, 5, 6,13,14);
        Integer dateDays = getDateDays(dt3.toDate(), dt3_1.toDate());
        System.out.println(dateDays);
    }

    @Data
    public static class Bean1 {
        String f1;
        String f2;
        String f3;
    }


    Integer getDateDays(Object firstTime, Object nowTime) {
        if(firstTime != null && nowTime != null){
            Calendar calendarMinDate = Calendar.getInstance();
            calendarMinDate.setTime((Date)firstTime);
            Calendar calendarMaxDate = Calendar.getInstance();
            calendarMaxDate.setTime((Date)nowTime);
            int days = (calendarMaxDate.get(Calendar.YEAR) - calendarMinDate.get(Calendar.YEAR)) * 365 +
                    calendarMaxDate.get(Calendar.DAY_OF_YEAR) - calendarMinDate.get(Calendar.DAY_OF_YEAR);
            return days;
        }
        return null;
    }
}



