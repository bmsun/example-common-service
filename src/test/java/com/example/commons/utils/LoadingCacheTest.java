package com.example.commons.utils;

import com.example.commons.example.LoadingCacheExample;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class LoadingCacheTest {


    @Test
    public void test(){
        LoadingCacheExample cacheExample =new LoadingCacheExample();
        cacheExample.getPeopleInfos("");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cacheExample.getPeopleInfos("");

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cacheExample.getPeopleInfos("");
    }

}
