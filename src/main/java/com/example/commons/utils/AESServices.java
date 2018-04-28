package com.example.commons.utils;


import com.example.commons.utils.exp.AES128;
import com.example.commons.utils.exp.AES256;
import com.example.commons.utils.exp.CipherText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/***
 * @Date 2018/4/28
 * @Description  比较成熟的AES加解密方式
 * @author zhanghesheng
 * */
public class AESServices {

    private Map<String, String> keyMap = new HashMap(256);
    private List<String> keyIds = new ArrayList();
    private static final String ALGORITHM_AES128 = "0";
    private static final String ALGORITHM_AES256 = "1";

    public AESServices() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("keyfile");
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        try {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String keyId = parts[0];
                String key = parts[1];
                this.keyMap.put(keyId, key);
                this.keyIds.add(keyId);
            }

            this.keyIds.sort(String::compareToIgnoreCase);
        } catch (IOException var15) {
            ;
        } finally {
            try {
                br.close();
            } catch (IOException var14) {
                ;
            }

        }

    }

    public Integer keySize() {
        return this.keyMap.size();
    }

    public List<String> listKeyIds(Predicate<String> p) {
        if (p == null) {
            p = (s) -> {
                return true;
            };
        }

        return (List)this.keyIds.stream().filter(p).collect(Collectors.toList());
    }

    private String getKey(String keyId) {
        return (String)this.keyMap.get(keyId);
    }

    public String encrypt128(String keyId, String content) {
        if (keyId != null && content != null) {
            String key = this.getKey(keyId);
            AES128 aes = new AES128(key);

            try {
                String cipher = aes.encrypt(content);
                return (new CipherText(keyId, "0", cipher)).toString();
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public byte[] encrypt128(String keyId, byte[] content) {
        if (keyId != null && content != null && content.length != 0) {
            String key = this.getKey(keyId);
            AES128 aes = new AES128(key);

            try {
                byte[] encryptBytes = aes.encrypt(content);
                byte[] result = new byte[encryptBytes.length + 4];
                byte[] prefixBytes = (keyId + "0").getBytes(StandardCharsets.UTF_8);
                System.arraycopy(prefixBytes, 0, result, 0, 4);
                System.arraycopy(encryptBytes, 0, result, 4, encryptBytes.length);
                return result;
            } catch (Exception var8) {
                var8.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String encrypt128(String content) {
        if (content == null) {
            return null;
        } else {
            int index = ThreadLocalRandom.current().nextInt(0, this.keyIds.size());
            String keyId = (String)this.keyIds.get(index);
            return this.encrypt128(keyId, content);
        }
    }

    public byte[] encrypt128(byte[] content) {
        if (content != null && content.length != 0) {
            int index = ThreadLocalRandom.current().nextInt(0, this.keyIds.size());
            String keyId = (String)this.keyIds.get(index);
            return this.encrypt128(keyId, content);
        } else {
            return null;
        }
    }

    /**
     * 异常原因：如果密钥大于128, 会抛出java.security.InvalidKeyException: Illegal key size 异常. 因为密钥长度是受限制的, java运行时环境读到的是受限的policy文件. 文件位于${java_home}/jre/lib/security, 这种限制是因为美国对软件出口的控制.
     * <p>
     * 解决方案：去官方下载JCE无限制权限策略文件。
     * jdk 5: http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#jce_policy-1.5.0-oth-JPR
     * <p>
     * jdk6: http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html
     * <p>
     * JDK7的下载地址: http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
     * JDK8的下载地址: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
     * <p>
     * 下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
     * 如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件
     * 如果安装了JDK，还要将两个jar文件也放到%JDK_HOME%\jre\lib\security\policy目录下覆盖原来文件。
     */
    public String encrypt256(String content) {
        if (content == null) {
            return null;
        } else {
            int index = ThreadLocalRandom.current().nextInt(0, this.keyIds.size());
            String keyId = (String)this.keyIds.get(index);
            return this.encrypt256(keyId, content);
        }
    }

    public String encrypt256(String keyId, String content) {
        if (keyId != null && content != null) {
            String key = this.getKey(keyId);
            AES256 aes = new AES256(key);

            try {
                String cipher = aes.encrypt(content);
                return (new CipherText(keyId, "1", cipher)).toString();
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String decrypt(String cipher) throws Exception {
        if (cipher == null) {
            return null;
        } else {
            CipherText cipherText = CipherText.fromString(cipher);
            if (cipherText == null) {
                return cipher;
            } else {
                String keyId = cipherText.getKeyId();
                String exp = cipherText.getBits();
                String content = cipherText.getText();
                String key = this.getKey(keyId);
                if (key == null) {
                    throw new IllegalArgumentException("key not found. keyId=" + keyId);
                } else if (exp.equals(ALGORITHM_AES128)) {
                    AES128 aes = new AES128(key);
                    return aes.decrypt(content);
                } else if (exp.equals(ALGORITHM_AES256)) {
                    AES256 aes = new AES256(key);
                    return aes.decrypt(content);
                } else {
                    return cipher;
                }
            }
        }
    }

    public byte[] decrypt(byte[] cipher) throws Exception {
        if (cipher == null) {
            return null;
        } else {
            CipherText cipherText = CipherText.fromBytes(cipher);
            if (cipherText == null) {
                return cipher;
            } else {
                String keyId = cipherText.getKeyId();
                String exp = cipherText.getBits();
                String key = this.getKey(keyId);
                /**解决Given final block not properly padded异常*/
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                if (key == null) {
                    throw new IllegalArgumentException("key not found. keyId=" + keyId);
                } else if (exp.equals(ALGORITHM_AES128)) {
                    AES128 aes = new AES128(key);
                    return aes.decrypt(cipherText.getData());
                } else if (exp.equals(ALGORITHM_AES256)) {
                    AES256 aes = new AES256(key);
                    return aes.decrypt(cipherText.getData());
                } else {
                    return cipher;
                }
            }
        }
    }
}
