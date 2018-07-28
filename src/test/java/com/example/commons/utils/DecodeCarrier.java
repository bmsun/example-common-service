package com.example.commons.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DecodeCarrier {

    @Test
    public void test(){
        String file = ReadConfigUtils.readFile("userInfo.txt");
        String[] splitArr = file.split("\n");

        List<String> newResultList =new ArrayList<>();
        for (int i=0;i<splitArr.length;i++){

                String[] strings = splitArr[i].split("\t");
            //String newResult= strings[0]+

        }
        //System.out.println(splitArr[0]);

    }
}
