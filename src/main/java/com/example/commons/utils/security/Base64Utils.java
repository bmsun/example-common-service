package com.example.commons.utils.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;


@Slf4j
public class Base64Utils {

    private static final String UTF_8 = "UTF-8";

    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error(inputData, e);
        }

        return null;
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static String encodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error(inputData, e);
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(Base64Utils.encodeData("A"));
        System.out.println(Base64Utils.decodeData("QQ=="));

    }
}
