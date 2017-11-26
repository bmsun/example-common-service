package com.example.commons;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseKeyValue {
    private static Map<String, InheritableThreadLocal> keyValues = new ConcurrentHashMap<>();

    public static void put(String key, Object value) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        InheritableThreadLocal keyValue;
        if (keyValues.containsKey(key)) {
            keyValue = keyValues.get(key);
        } else {
            keyValue = new InheritableThreadLocal();
            keyValues.put(key, keyValue);
        }
        keyValue.set(value);
    }

    public static Object get(String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key不能为空");
        if (!keyValues.containsKey(key)) {
            return null;
        }
        return keyValues.get(key).get();
    }
}
