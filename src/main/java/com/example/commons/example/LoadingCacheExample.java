package com.example.commons.example;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

/***
 * @Date 2017/11/24
 *@Description guava 缓存机制案例
 * @author zhanghesheng
 * */
public class LoadingCacheExample {
    /**
     * 5秒自动过期
     */
    LoadingCache<String, List<Bean>> cache = CacheBuilder.newBuilder()
             //设置过期时间
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, List<Bean>>() {
                @Override
                public List<Bean> load(String key) throws Exception {
                    return getPeopleInfos(key);
                }
    });

     //真正调用的方法
    public List<Bean> getPeopleInfos(String key) {
        List<Bean> beans = null;
        try {
            beans = this.cache.get(key);
        } catch (Exception e) {
            // ignore
        }
        if (beans == null) {
            //这里执行查询数据库等获取数据的逻辑
            Bean bean =new Bean();
            bean.setB1("b1");
            bean.setB2("b2");
            beans.add(bean);
        }

        return beans;
    }

    @Data
    public static class Bean {
        String b1;
        String b2;
    }

}
