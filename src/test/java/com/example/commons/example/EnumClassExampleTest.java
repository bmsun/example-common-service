package com.example.commons.example;

import org.junit.Test;

import java.util.Arrays;
/***
 * @Date 2018/5/17
 * @Description  含参枚举类使用案例
 * @author zhanghesheng
 * */
public class EnumClassExampleTest {

    @Test
    public void testValueOf(){
        System.out.println(EnumClassExample.valueOf("ENUM_1"));//ENUM_1
        //等同
        System.out.println(EnumClassExample.ENUM_1);
        //等同
        System.out.println(EnumClassExample.ENUM_1.name());
    }

    @Test
    public void testValues(){
        EnumClassExample[] values = EnumClassExample.values();
        Arrays.stream(values).forEach(System.out::println);
    }

    @Test
    public void testParamName(){
        System.out.println(EnumClassExample.ENUM_1.getParamName());

    }
}
