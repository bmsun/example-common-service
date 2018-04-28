package com.example.commons.utils.exp;

import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class CipherText {

    private String keyId;
    private String bits;
    private String text;
    private byte[] data;

    public static CipherText fromString(String s) {
        if (s.length() > 4) {
            String keyId = s.substring(0, 3);
            String bits = s.substring(3, 4);
            String content = s.substring(4);
            return new CipherText(keyId, bits, content);
        } else {
            return null;
        }
    }

    public static CipherText fromBytes(byte[] b) {
        if (b.length > 4) {
            String s = new String(b, 0, 4, StandardCharsets.UTF_8);
            String keyId = s.substring(0, 3);
            String bits = s.substring(3, 4);
            byte[] data = new byte[b.length - 4];
            System.arraycopy(b, 4, data, 0, b.length - 4);
            return new CipherText(keyId, bits, data);
        } else {
            return null;
        }
    }

    public CipherText(String keyId, String bits, byte[] data) {
        this.keyId = keyId;
        this.bits = bits;
        this.data = data;
    }

    public CipherText(String keyId, String bits, String text) {
        this.keyId = keyId;
        this.bits = bits;
        this.text = text;
    }

    @Override
    public String toString() {
        return this.keyId + this.bits + this.text;
    }
}
