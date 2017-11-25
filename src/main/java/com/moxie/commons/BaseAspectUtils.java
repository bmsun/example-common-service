package com.moxie.commons;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

/**
 *
 */
@Slf4j
public class BaseAspectUtils {
    public static Object logAround(ProceedingJoinPoint joinPoint, Long maxTimeInMillis) throws Throwable {
        final long start = System.currentTimeMillis();
        String methodName = joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName();
        log.info("{}开始", methodName);
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            StringBuilder methodInfo = new StringBuilder(methodName);
            if (joinPoint.getArgs() != null) {
                methodInfo.append("(").append(Arrays.asList(joinPoint.getArgs())).append(")");
            }
            log.error("方法{}调用异常", methodInfo.toString());
            throw e;
        } finally {
            long timeUsed = System.currentTimeMillis() - start;
            if (timeUsed > maxTimeInMillis) {
                log.warn("{}结束, 所花时间: {}ms", methodName, timeUsed);
            }
        }
    }
}
