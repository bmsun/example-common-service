package com.example.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by zhanghesheng
 */
@Slf4j
public class BaseJsonUtilsTest {
    @Test
    public void test() {
        Map<String, Object> map = ImmutableMap.of("type", "service", "version", 1);
        String json = BaseJsonUtils.writeValue(map);
        map = BaseJsonUtils.readValue(json, Map.class);
        Assert.assertTrue(map.containsKey("type"));
        Assert.assertEquals(map.get("type"), "service");
        Assert.assertTrue(map.containsKey("version"));
        Assert.assertEquals(map.get("version"), 1);

        map = ImmutableMap.of("f1", "hello");
        Bean bean = BaseJsonUtils.readValue(map, Bean.class);
        Assert.assertEquals(bean.getF1(), "hello");

        Assert.assertEquals(BaseJsonUtils.valueFromJsonKey(json, "type"), "service");
    }


    @Test
    public void testJackson() throws Exception{
        ObjectMapper  mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(new Bean());
        System.out.println(s);
        System.out.println(mapper.writeValueAsString("222"));

    }

    @Data
    public static class Bean {
        String f1;
    }
}
