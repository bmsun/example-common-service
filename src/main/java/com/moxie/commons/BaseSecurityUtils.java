package com.moxie.commons;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class BaseSecurityUtils {
    public static String md5(String src) {
        if (StringUtils.isBlank(src)) {
            return src;
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(src.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            String strTemp;
            for (int i = 0; i < digest.length; i++) {
                strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
                hexString.append(strTemp);
            }
            return hexString.toString();
        } catch (Throwable e) {
            throw new RuntimeException("md5异常", e);
        }
    }

    public static String base64Encode(String src) {
        if (StringUtils.isBlank(src)) {
            return src;
        }

        try {
            return Base64.getEncoder().encodeToString(src.getBytes("UTF-8"));
        } catch (Throwable e) {
            throw new RuntimeException("base64 encode异常", e);
        }
    }

    public static String base64Decode(String src) {
        if (StringUtils.isBlank(src)) {
            return src;
        }

        try {
            return new String(Base64.getDecoder().decode(src), "UTF-8");
        } catch (Throwable e) {
            throw new RuntimeException("base64 decode异常", e);
        }
    }

    /**
     * aes加密-128位
     */
    public static String aesEncrypt(String src, String pass) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pass), "密钥不能为空");
        if (StringUtils.isBlank(src)) {
            return src;
        }

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(pass.getBytes());
            keyGen.init(128, secureRandom);
            SecretKey secretKey = keyGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return byte2Hex(cipher.doFinal(src.getBytes("UTF-8")));
        } catch (Throwable e) {
            throw new RuntimeException("aes encode异常", e);
        }
    }

    /**
     * aes解密-128位
     */
    public static String aesDecrypt(String src, String pass) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pass), "密钥不能为空");
        if (StringUtils.isBlank(src)) {
            return src;
        }

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(pass.getBytes());
            keyGen.init(128, secureRandom);
            SecretKey secretKey = keyGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(hex2Bytes(src)));
        } catch (Throwable e) {
            throw new RuntimeException("aes decode异常", e);
        }
    }

    /**
     * 将byte[] 转换成字符串
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : bytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * 将16进制字符串转为转换成字符串
     */
    private static byte[] hex2Bytes(String src) {
        byte[] sourceBytes = new byte[src.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(src.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }
}
