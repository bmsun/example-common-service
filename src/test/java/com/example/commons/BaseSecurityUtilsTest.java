package com.example.commons;


import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wangyanbo on 16/12/9.
 */
public class BaseSecurityUtilsTest {
    @Test
    public void test() {
        Assert.assertNull(BaseSecurityUtils.md5(null));
        Assert.assertEquals(BaseSecurityUtils.md5(""), "");
        Assert.assertTrue(BaseSecurityUtils.md5("test").length() > 0);

        String src = "test中文";
        String des = BaseSecurityUtils.base64Encode(src);
        Assert.assertEquals(BaseSecurityUtils.base64Decode(des), src);

        String secret = "secret";
        des = BaseSecurityUtils.aesEncrypt(src, secret);
        Assert.assertEquals(BaseSecurityUtils.aesDecrypt(des, secret), src);
    }
}
