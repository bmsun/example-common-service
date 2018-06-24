package com.example.commons.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynTest {

    private final ExecutorService asynActor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("dao-singleWriteAsyn-%d").build());


    /**
     *
     *异步方法测试
     */
    @Test
    public  void testAsyn(){
        try {
            asynMethod();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("main thread end");

    }


    private void asynMethod(){

        asynActor.submit(()->{
            System.out.println("asyn Thread start");
            try {
                int  a= 2/0;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            System.out.println("asyn Thread end");
        });
    }
}
