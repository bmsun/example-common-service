package com.example.commons.utils;

import org.junit.Test;

public class ReadConfigUtilsTest {


    @Test
    public void testReadFile() throws Exception {
        String s = ReadConfigUtils.readFile("/Users/zhanghesheng/Documents/private/example-common-service/src/main/resources/test.html");
        System.out.println(s);

    }

    @Test
    public void testReadFileByLines() throws Exception {
        String s = ReadConfigUtils.readFileByLines("testfile/test.html");
        System.out.println(s);

    }

    @Test
    public void testWriteFile() throws Exception {
       ReadConfigUtils.writeFile("","", "testfile/test.html");
        //System.out.println(s);

    }

}
