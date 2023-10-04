package springinfra.aspect;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import springinfra.SpringContext;
import springinfra.annotation.validation.SecureProperty;
import springinfra.model.dto.GenericDto;
import springinfra.security.validator.SecurePropertyValidator;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FieldLevelSecurityAspect implements BaseAspect {

    @Pointcut("@annotation(springinfra.annotation.FieldLevelSecurity)")
    public void annotatedMethod() {
    }

    @Pointcut("@within(springinfra.annotation.FieldLevelSecurity)")
    public void annotatedClass() {
    }

    //    @Around(value = "@annotation(springinfra.annotation.FieldLevelSecurity)")
    @Around("execution(* *(..)) && (annotatedMethod() || annotatedClass())")
    public Object interceptResponse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object returnValue = proceedingJoinPoint.proceed();

        if (returnValue instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();
            if (body != null) {
                GenericDto responseBody = SpringContext.getApplicationContext().getBean(ObjectMapper.class).convertValue(body, GenericDto.class);

                returnValue = new ResponseEntity<>(filter(responseBody.getProperties(), body), responseEntity.getHeaders(), responseEntity.getStatusCode());
            }
        } else {
            throw new OperationNotSupportedException("Field level security is not available for other types except ResponseEntity");
        }

        return returnValue;
    }

    private GenericDto filter(Map<String, Object> input, Object object) throws IllegalAccessException {
        GenericDto result = new GenericDto();
        List<Field> fields = Arrays.asList(object.getClass().getDeclaredFields());

        for (Map.Entry<String, Object> entry : input.entrySet()) {
            Optional<Field> optionalField = getRespectiveField(fields, entry.getKey());

            if (optionalField.isEmpty()) {
                result.setProperty(entry.getKey(), entry.getValue());
                continue;
            }
            Field field = optionalField.get();

            List<Object> entryValueList = new ArrayList<>();
            boolean isListType = false;
            if (entry.getValue() != null && Collection.class.isAssignableFrom(entry.getValue().getClass())) {
                entryValueList.addAll((Collection<?>) entry.getValue());
                isListType = true;
            } else {
                entryValueList.add(entry.getValue());
            }

            field.setAccessible(true);
            Object filedObject = field.get(object);
            if (filedObject instanceof Collection<?> collection && !collection.isEmpty()) {
                filedObject = collection.iterator().next();
            }
            field.setAccessible(false);

            List<Object> filteredValuesPerKey = new ArrayList<>();
            for (Object value : entryValueList) {
                if (value != null && Map.class.isAssignableFrom(value.getClass())) {
                    GenericDto innerDto = filter((Map<String, Object>) value, filedObject);

                    filteredValuesPerKey.add(innerDto.getProperties());
                } else {
                    Optional<Method> getterMethod = getRespectiveGetterMethod(object.getClass(), field);
                    if (field.isAnnotationPresent(SecureProperty.class) || (getterMethod.isPresent() && getterMethod.get().isAnnotationPresent(SecureProperty.class))) {
                        SecureProperty securePropertyAnnotation = field.isAnnotationPresent(SecureProperty.class) ? field.getAnnotation(SecureProperty.class) : getterMethod.get().getAnnotation(SecureProperty.class);
                        if (!SecurePropertyValidator.isAccessible(securePropertyAnnotation)) {
                            continue;
                        }
                    }

                    filteredValuesPerKey.add(value);
                }
            }
            if (entryValueList.isEmpty()) {
                Optional<Method> getterMethod = getRespectiveGetterMethod(object.getClass(), field);
                if (field.isAnnotationPresent(SecureProperty.class) || (getterMethod.isPresent() && getterMethod.get().isAnnotationPresent(SecureProperty.class))) {
                    SecureProperty securePropertyAnnotation = field.isAnnotationPresent(SecureProperty.class) ? field.getAnnotation(SecureProperty.class) : getterMethod.get().getAnnotation(SecureProperty.class);
                    if (!SecurePropertyValidator.isAccessible(securePropertyAnnotation)) {
                        continue;
                    }
                }

                result.setProperty(entry.getKey(), entryValueList);
            } else if (filteredValuesPerKey.size() == 1 && !isListType) {
                result.setProperty(entry.getKey(), filteredValuesPerKey.get(0));
            } else if (!filteredValuesPerKey.isEmpty()) {
                result.setProperty(entry.getKey(), filteredValuesPerKey);
            }
        }

        return result;
    }

    private Optional<Field> getRespectiveField(List<Field> fields, String jsonFieldName) {
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(jsonFieldName) ||
                    (field.isAnnotationPresent(JsonProperty.class) && jsonFieldName.equalsIgnoreCase(field.getAnnotation(JsonProperty.class).value()))) {
                return Optional.of(field);
            }
        }

        return Optional.empty();
    }

    private Optional<Method> getRespectiveGetterMethod(Class<?> classType, Field field) {
        String getterMethodName = "get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);

        try {
            return Optional.of(classType.getDeclaredMethod(getterMethodName));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }
}
