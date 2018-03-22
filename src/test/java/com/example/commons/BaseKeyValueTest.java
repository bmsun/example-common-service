package com.example.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhanghesheng
 */
public class BaseKeyValueTest {
    @Test
    public void test() {
        BaseKeyValue.put("key", "value");
        new Thread(() -> {
            Assert.assertEquals(BaseKeyValue.get("key"), "value");
        }).start();
    }
}
