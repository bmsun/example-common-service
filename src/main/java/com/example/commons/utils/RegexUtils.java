package com.example.commons.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *@Description
 * @author zhanghesheng
 * */
public class RegexUtils {
    private static  Logger LOGGER = LoggerFactory.getLogger(RegexUtils.class);

    /***
     *匹配到第一个符合规则的内容就结束并返回
     *@param content 需要匹配内容
     * @param index 需要匹配内容的下标
     * @param regex 正则表达式
     * */
    public static String regexPattern(String regex, String content, int index) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            try {
                result = matcher.group(index);
            } catch (IndexOutOfBoundsException e) {
                LOGGER.error("参数:index[{}]传入有误，请确认是否存在此下标",index,e);
                e.printStackTrace();
            }
        }
        return result;
    }

    /***
     *匹配到第一个符合规则的内容就结束并返回
     *@param content 需要匹配内容
      * @param regex 正则表达式
     * */
    public static String regexPattern(String regex, String content) {
      return regexPattern(regex,content,0);
    }

       /***
     *匹配所有符合规则的内容
     *@param content 需要匹配内容
     * @param index 需要匹配内容的下标
     * @param regex 正则表达式
     * @return List<String> 匹配规则结果集
     * */
    public static List<String> regexAllPattern(String regex, String content, int index) {
        List<String> arrayList = Lists.newArrayList();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String result;
            try {
                result = matcher.group(index);
                arrayList.add(result);
            } catch (IndexOutOfBoundsException e) {
                LOGGER.error("参数:index[{}]传入有误，请确认是否存在此下标",index,e);
                e.printStackTrace();
            }

        }
        return arrayList;
    }

    /***
     *匹配所有符合规则的内容
     *@param content 需要匹配内容
     * @param regex 正则表达式
     * @return List<String> 匹配规则结果集
     * */
    public static List<String> regexAllPattern(String regex, String content) {
        return regexAllPattern(regex, content,0);
    }


    public static void main(String[] args) {
        String regex ="(\\d+(\\D)*(\\d+))";
        String content="23d45";
        String result = RegexUtils.regexPattern(regex, content, 1);
        System.out.println(result);
    }
}
