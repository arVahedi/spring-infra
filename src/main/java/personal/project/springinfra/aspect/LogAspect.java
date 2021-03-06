package personal.project.springinfra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect extends BaseAspect {

    @Before("execution(* personal.project.springinfra..*.*(..))")
    public void logMethodCalled(JoinPoint joinPoint) {
        log.debug("{} method called with args {}.", joinPoint.getSignature().toShortString(), Arrays.asList(joinPoint.getArgs()));
    }

    @AfterReturning(value = "execution(* personal.project.springinfra..*.*(..))", returning = "returnValue")
    public void logMethodReturned(JoinPoint joinPoint, Object returnValue) {
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
            log.debug("{} method returned. [{}]", joinPoint.getSignature().toShortString(), "A LARGE COLLECTION/MAP OBJECT");
        } else {
            log.debug("{} method returned. [{}]", joinPoint.getSignature().toShortString(), returnValue);
        }
    }
}

