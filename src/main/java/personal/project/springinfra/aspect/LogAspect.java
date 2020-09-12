package personal.project.springinfra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        log.debug("{} method returned. [{}]", joinPoint.getSignature().toShortString(), returnValue);
    }
}

