package com.example.commons.utils;

import org.junit.Before;
import org.junit.Test;


public class AESServicesTest {
    private AESServices aesServices;

    @Before
    public void init(){
        aesServices =new AESServices();
    }

    @Test
    public void test() throws Exception{
       String t= aesServices.encrypt128("1");
        System.out.println(t);
        System.out.println(aesServices.decrypt(t));
        System.out.println("================================");

        String t1= aesServices.encrypt256("1");
        System.out.println(t1);
        System.out.println(aesServices.decrypt(t1));
    }
}
