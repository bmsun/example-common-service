package com.example.commons.utils.exp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AES128 {
    private SecretKey secretKeySpec;
    public static final String AES = "AES/CBC/PKCS5Padding";
    public static final int KEY_LENGTH = 128;

    public AES128(String secretKey) {
        this.setSecretKey(secretKey);
    }

    public void setSecretKey(String secretKey) {
        String secret = secretKey.trim();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] sha1 = messageDigest.digest(secret.getBytes(StandardCharsets.UTF_8));
            byte[] keyBytes = new byte[16];
            System.arraycopy(sha1, 0, keyBytes, 0, keyBytes.length);
            this.secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
        }

    }

    public byte[] encrypt(byte[] text) throws Exception {
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, this.secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(text);
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);
        return encryptedIVAndText;
    }

    public String encrypt(String text) throws Exception {
        byte[] encryptBytes = this.encrypt(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptBytes);
    }

    public String decrypt(String encryptedText) throws Exception {
        int ivSize = 16;
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedText);
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(2, this.secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);
        return new String(decrypted);
    }

    public byte[] decrypt(byte[] b) throws Exception {
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        System.arraycopy(b, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        int encryptedSize = b.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(b, ivSize, encryptedBytes, 0, encryptedSize);
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(2, this.secretKeySpec, ivParameterSpec);
        return cipherDecrypt.doFinal(encryptedBytes);
    }
}
