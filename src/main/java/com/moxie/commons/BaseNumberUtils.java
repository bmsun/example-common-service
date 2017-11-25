package com.moxie.commons;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Created by wangyanbo on 16/11/14.
 */
@Slf4j
public class BaseNumberUtils {
    public static Long yuanToFen(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.multiply(new BigDecimal(100)).longValue();
    }

    public static Long yuanToFenNotNull(BigDecimal amount) {
        if (amount == null) {
            return 0l;
        }
        return amount.multiply(new BigDecimal(100)).longValue();
    }

    public static int compare(BigDecimal num1, BigDecimal num2) {
        if (num1 == null && num2 == null) {
            return 0;
        }
        if (num1 == null) {
            return -1;
        }
        if (num2 == null) {
            return 1;
        }
        return num1.compareTo(num2);
    }
}
