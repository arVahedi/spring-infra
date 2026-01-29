package org.springinfra.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * This class is used for debugging purposes. Actually it's an aspect around of all existing methods of the application
 * <strong>(expect the filters)</strong> that can log all method calls with their input parameters as well as their output.
 * <p>
 * Keep in mind that this aspect doesn't work with filters because as it proxies the filter, the init method is final cannot be proxied,
 * will thus be executed on the proxy instead of being passed through to the actual object.
 * The proxy doesn't have any of its fields initialized (no need to) and thus here we get a NullPointerException.
 * So we have to exclude the filter package for solving this issue.
 */

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect implements BaseAspect {

    @Before("execution(* springinfra..*.*(..)) && !execution(* springinfra.controller.filter..*..*(..))")
    public void logMethodCalled(JoinPoint joinPoint) {
        Logger logger = getJoinPointLogger(joinPoint).orElse(log);
        logger.debug("{} method called with args {}.", joinPoint.getSignature().toShortString(), Arrays.asList(joinPoint.getArgs()));
    }

    @AfterReturning(value = "execution(* springinfra..*.*(..)) && !execution(* springinfra.controller.filter..*..*(..))", returning = "returnValue")
    public void logMethodReturned(JoinPoint joinPoint, Object returnValue) {
        Logger logger = getJoinPointLogger(joinPoint).orElse(log);
        int returnValueSize = 1;
        if (returnValue != null) {
            if (returnValue instanceof Collection<?>) {
                returnValueSize = ((Collection) returnValue).size();
            } else if (returnValue instanceof Map<?, ?>) {
                returnValueSize = ((Map) returnValue).size();
            } else if (returnValue.getClass().isArray()) {
                //
            }
        }

        if (returnValueSize > 10000) {
            logger.debug("{} method returned. [{}]", joinPoint.getSignature().toShortString(), "A LARGE COLLECTION/MAP OBJECT");
        } else {
            logger.debug("{} method returned. [{}]", joinPoint.getSignature().toShortString(), returnValue);
        }
    }

    private Optional<Logger> getJoinPointLogger(JoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getSignature().getDeclaringType();
        Optional<Logger> result = Optional.ofNullable(LoggerFactory.getLogger(targetClass));

        if (result.isEmpty()) {
            log.warn("Could find logger field in the class {}, the method log would be written by local logger of {}", targetClass.getName(), this.getClass().getName());
        }
        return result;
    }
}

