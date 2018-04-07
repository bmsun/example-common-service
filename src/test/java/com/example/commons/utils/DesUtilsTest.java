package com.example.commons.utils;

import org.junit.Test;

public class DesUtilsTest {

    @Test
    public  void testDes() {
        String str = "01234ABCDabcd!@#$";
        String t = "";
        System.out.println("加密后：" + (t = DESUtils.instance.encrypt(str)));
        System.out.println("解密后：" + DESUtils.instance.decrypt(t));
    }
}
