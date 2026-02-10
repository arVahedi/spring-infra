package org.springinfra.security.validator;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springinfra.annotation.validation.ValidateAs;
import org.springinfra.model.dto.request.PropertyBagRequest;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator for {@link PropertyBagRequest}
 * <p>
 * Ensures only fields defined in the target DTO class are present
 */

@Slf4j
@RequiredArgsConstructor
public class ValidateAsValidator extends BaseValidator<ValidateAs, PropertyBagRequest> {

    private final Validator validator;

    private ValidateAs constraintAnnotation;

    @Override
    public void initialize(final ValidateAs constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(final PropertyBagRequest propertyBagRequest, final ConstraintValidatorContext constraintValidatorContext) {
        if (propertyBagRequest == null) {
            return true;
        }

        Class<?> delegateClass = this.constraintAnnotation.value();
        Set<ConstraintViolation<?>> constraintViolations = validateMapFields(delegateClass, propertyBagRequest.getProperties());
        constraintViolations.addAll(validateMapValues(delegateClass, propertyBagRequest.getProperties(), null, this.constraintAnnotation.groups()));

        if (!constraintViolations.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            for (ConstraintViolation<?> violation : constraintViolations) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(MessageFormat.format("[{0}]: {1}", violation.getPropertyPath(), violation.getMessage()))
                        .addConstraintViolation();
            }
        }

        return constraintViolations.isEmpty();
    }

    private Set<ConstraintViolation<?>> validateMapFields(Class<?> delegateClass, Map<String, Object> properties) {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

        // Extract all field names from the target class
        Set<String> allowedFields = Arrays.stream(delegateClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        // Check if all provided properties are allowed
        Set<String> providedFields = properties.keySet();

        for (String field : providedFields) {
            if (!allowedFields.contains(field)) {
                log.error("Field {} is not allowed for {}", field, delegateClass.getSimpleName());
                var message = "Property does not exist";
                constraintViolations.add(ConstraintViolationImpl.forBeanValidation(message, null, null, message, delegateClass, null, null,
                        field, PathImpl.createPathFromString(field), null, null));
            }
        }

        return constraintViolations;
    }

    private Set<ConstraintViolation<?>> validateMapValues(Class<?> delegateClass, Map<String, Object> values, String prefix, Class<?>... groups) {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String fieldName = StringUtils.isBlank(prefix) ? entry.getKey() : prefix + "." + entry.getKey();

            if ((entry.getValue() != null) && Map.class.isAssignableFrom(entry.getValue().getClass())) {
                constraintViolations.addAll(validateMapValues(delegateClass, (Map<String, Object>) entry.getValue(), entry.getKey(), groups));
            } else {
                try {
                    constraintViolations.addAll(this.validator.validateValue(delegateClass, fieldName, entry.getValue(), groups));
                } catch (IllegalArgumentException ex) {
                    String message = "Property does not exist";
                    constraintViolations.add(ConstraintViolationImpl.forBeanValidation(message, null, null, message, delegateClass, null, null,
                            entry.getValue(), PathImpl.createPathFromString(fieldName), null, null));
                }
            }
        }

        return constraintViolations;
    }
}
