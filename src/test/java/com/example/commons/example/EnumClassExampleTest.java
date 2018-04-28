package com.example.commons.example;

import org.junit.Test;

import java.util.Arrays;

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
