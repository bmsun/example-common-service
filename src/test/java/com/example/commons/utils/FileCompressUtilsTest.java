package com.example.commons.utils;

import org.junit.Test;

import java.io.File;

public class FileCompressUtilsTest {

    @Test
    /**加密压缩*/
    public void testPassZip() throws Exception{
        File file = FileCompressUtils.encryptZip("/Users/zhanghesheng/Desktop/任务统计分析",  null);
        System.out.println(file.getPath());
    }


    @Test
    /**解压缩*/
    public void testPassUnZip() throws Exception{
        FileCompressUtils.encryptUnZip("/Users/zhanghesheng/Desktop/任务统计分析.zip", "/Users/zhanghesheng/Desktop", null);
    }

}
