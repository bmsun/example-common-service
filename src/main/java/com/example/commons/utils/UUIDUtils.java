package com.example.commons.utils;


import com.netflix.astyanax.util.TimeUUIDUtils;
import org.joda.time.DateTime;

import java.util.UUID;

/***
 * @Date 2018/5/17
 * @Description  UUID工具类
 * @author zhanghesheng
 * */
public class UUIDUtils {

    /**
     * 生成当前时间对应的uuid
     *
     * @return uuid
     */
    public static String getUniqueTimeUUIDinMicros() {
        return TimeUUIDUtils.getUniqueTimeUUIDinMicros().toString();
    }

    /**
     * 将uuid转换成指定的时间格式
     *
     * @param uuid   uuid
     * @param format 时间格式
     * @return
     */
    public static String getMillsTimeFromUUID(String uuid, String format) {
        //return 毫秒值
        long timeUUID = TimeUUIDUtils.getTimeFromUUID(UUID.fromString(uuid));
        DateTime dateTime = new DateTime(timeUUID);
        return dateTime.toString(format);
    }

    /**
     * 将uuid转换成指定的时间格式,默认yyyy/MM/dd HH:mm:ss SS(EE:星期 -> SS:毫秒)
     *
     * @param uuid uuid
     * @return
     */
    public static String getMillsTimeFromUUID(String uuid) {
        return getMillsTimeFromUUID(uuid, "yyyy/MM/dd HH:mm:ss SS");
    }

    public static void main(String[] args) {
        // 获取当前时间对应的uuid
        System.out.println(getUniqueTimeUUIDinMicros());
        System.out.println(getUniqueTimeUUIDinMicros().length());

        // 将uuid转换成指定的时间格式,默认yyyy/MM/dd HH:mm:ss
        System.out.println(getMillsTimeFromUUID(getUniqueTimeUUIDinMicros()));

        // 将uuid转换成指定的时间格式
        System.out.println(getMillsTimeFromUUID("f358fcf0-ba68-11e7-9560-00163e0f4efb", "yyyy/MM/dd HH:mm:ss SS"));
    }

}
