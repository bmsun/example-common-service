package com.example.commons.utils;

import com.example.commons.utils.security.DESUtils;
import org.junit.Test;

public class DesUtilsTest {

    @Test
    public  void testDes() {
        String str = "01234ABCDabcd!@#$";
        String t = "";
        System.out.println("加密后：" + (t = DESUtils.instance.encrypt(str)));
        System.out.println("解密后：" + DESUtils.instance.decrypt(t));
    }

    @Test
    public  void testDesWithKey() {
        String str = "01234ABCDabcd!@#$";
        String key = "abcDEF120";
        String t = "";
        System.out.println("加密后：" + (t = DESUtils.instance.encrypt(str,key)));
        System.out.println("解密后：" + DESUtils.instance.decrypt(t,key));
    }
}
