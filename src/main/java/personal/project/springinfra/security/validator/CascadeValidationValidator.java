package personal.project.springinfra.security.validator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import personal.project.springinfra.annotation.validation.CascadeValidation;
import personal.project.springinfra.assets.VirtualValidationGroups;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CascadeValidationValidator extends BaseValidator<CascadeValidation, Object> {

    private final Validator validator;

    private CascadeValidation constraintAnnotation;

    @Override
    public void initialize(CascadeValidation constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

        List<Class> validationGroups = new ArrayList<>();
        for (Class<?> group : this.constraintAnnotation.cascadeGroups()) {
            if (VirtualValidationGroups.class.isAssignableFrom(group)) {
                validationGroups.addAll(Arrays.asList(((VirtualValidationGroups) group.getConstructor().newInstance()).actualGroups(object)));
            } else {
                validationGroups.add(group);
            }
        }

        Set<ConstraintViolation<Object>> constraintViolations = this.validator.validate(object, validationGroups.toArray(new Class[validationGroups.size()]));

        if (!CollectionUtils.isEmpty(constraintViolations)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            for (ConstraintViolation<Object> violation : constraintViolations) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(violation.getMessageTemplate())
                        .addConstraintViolation();
            }
        }

        return CollectionUtils.isEmpty(constraintViolations);
    }
}
