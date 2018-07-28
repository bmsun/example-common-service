package com.example.commons.utils;

import org.junit.Test;

import java.io.IOException;

public class ShellTest {
    @Test
    public void shellTest(){
        Process exec =null;
        try {
             exec = Runtime.getRuntime().exec("nohup java -jar /Users/zhanghesheng/Documents/burpsuite_free_v1.7.01beta.jar >>/dev/null 2>&1 &");
            exec.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(exec.exitValue());
    }
}
