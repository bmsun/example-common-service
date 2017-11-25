package com.moxie.commons.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.moxie.commons.BaseDateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by wangyanbo on 16/11/9.
 */
public class FromDateFormat extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        String date = jp.getText();
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return BaseDateUtils.parseDate(date, "yyyy-MM-dd");
    }
}
