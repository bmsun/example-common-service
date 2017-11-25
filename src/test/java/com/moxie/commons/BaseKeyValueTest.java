package com.moxie.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wangyanbo on 17/2/23.
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
