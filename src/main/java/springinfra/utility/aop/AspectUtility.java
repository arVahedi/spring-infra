package springinfra.utility.aop;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class AspectUtility {

    public Optional<Object> getMethodParameterByName(ProceedingJoinPoint joinPoint, String parameterName) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parametersName = methodSignature.getParameterNames();

        int index = Arrays.asList(parametersName).indexOf(parameterName);

        Object[] args = joinPoint.getArgs();
        if (args.length > index) {
            return Optional.of(args[index]);
        }

        return Optional.empty();
    }
}
