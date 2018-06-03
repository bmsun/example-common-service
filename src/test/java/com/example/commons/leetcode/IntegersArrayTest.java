package com.example.commons.leetcode;

import org.junit.Test;

import java.util.*;

/***
 * 题目：
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 * You may assume that each input would have exactly one solution.
 *
 * Example:
 * Given nums = [2, 7, 11, 15], target = 9,
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 * 翻译：
 * 给定一个整形数组和一个整数target，返回2个元素的下标，它们满足相加的和为target。
 * 你可以假定每个输入，都会恰好有一个满足条件的返回结果。
 *
 *
 * */
public class IntegersArrayTest {

      /**
       *
       */
    public List<String> twoSum(int[] nums, int target) {
        List<String> result=new ArrayList<>();
        loop:
        for(int i=0;i<nums.length;i++){
          for (int j=i+1;j<nums.length;j++){
              if(nums[i]+nums[j]==target){
                  result.add(i+"+"+j);
                  //1、如果只需要找到一组符合条件就结束，跳出；
                  //2、如果只需要找到所有符合条件的，则不需break；
                 // break loop;
              }
              System.out.println("你未跳出内层循环");
          }
            System.out.println("你未跳出外层循环");
        }
        return result;
    }


    @Test
    public void test(){
        int[] nums ={11,9,2,8,18,12,11,9,2,8,18,12,11,9,2,8,18,12,11,9,2,8,18,12};
        long t1=System.currentTimeMillis();
        List<String> list = twoSum(nums, 20);
        System.out.println(System.currentTimeMillis()-t1);
        list.forEach(System.out::println);

    }
}
