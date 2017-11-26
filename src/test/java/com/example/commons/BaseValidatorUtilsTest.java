package com.example.commons;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ValidationException;
import javax.validation.constraints.Min;

/**
 * Created by wangyanbo on 17/2/23.
 */
public class BaseValidatorUtilsTest {
    @Test
    public void test() {
        try {
            Bean bean = new Bean();
            bean.setF2(1);
            BaseValidatorUtils.validate(bean);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(e instanceof ValidationException);
            Assert.assertTrue(e.getMessage().contains("不能为空"));
            Assert.assertTrue(e.getMessage().contains("小于5"));
        }
    }

    @Data
    public static class Bean {
        @NotBlank(message = "不能为空")
        private String f1;
        @Min(5)
        private Integer f2;
    }
}
