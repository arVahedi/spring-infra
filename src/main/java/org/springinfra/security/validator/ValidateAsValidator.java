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
import org.springinfra.model.dto.GenericDto;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ValidateAsValidator extends BaseValidator<ValidateAs, Object> {

    private final Validator validator;

    private ValidateAs constraintAnnotation;

    @Override
    public void initialize(final ValidateAs constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext constraintValidatorContext) {
        try {
            Class<?> delegateClass = Class.forName(this.constraintAnnotation.value());
            if (object.getClass().isAssignableFrom(GenericDto.class)) {
                GenericDto genericDto = (GenericDto) object;

                Set<ConstraintViolation<?>> constraintViolations = validateMap(delegateClass, genericDto.getProperties(), null, this.constraintAnnotation.withGroups());

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
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }

    private Set<ConstraintViolation<?>> validateMap(Class<?> delegateClass, Map<String, Object> values, String prefix, Class<?>... groups) {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String fieldName = StringUtils.isBlank(prefix) ? entry.getKey() : prefix + "." + entry.getKey();

            if ((entry.getValue() != null) && Map.class.isAssignableFrom(entry.getValue().getClass())) {
                constraintViolations.addAll(validateMap(delegateClass, (Map<String, Object>) entry.getValue(), entry.getKey(), groups));
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
