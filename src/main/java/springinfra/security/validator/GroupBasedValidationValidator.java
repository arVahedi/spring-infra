package springinfra.security.validator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import springinfra.annotation.validation.GroupBasedValidation;
import springinfra.assets.AbstractiveValidationGroup;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class GroupBasedValidationValidator extends BaseValidator<GroupBasedValidation, Object> {

    private final Validator validator;

    private GroupBasedValidation constraintAnnotation;

    @Override
    public void initialize(GroupBasedValidation constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {

        List<Class> validationGroups = new ArrayList<>();
        for (Class<?> group : this.constraintAnnotation.value()) {
            if (AbstractiveValidationGroup.class.isAssignableFrom(group)) {
                validationGroups.addAll(Arrays.asList(((AbstractiveValidationGroup) group.getConstructor().newInstance()).getGroups(object)));
            } else {
                validationGroups.add(group);
            }
        }

        Set<ConstraintViolation<Object>> constraintViolations = this.validator.validate(object, validationGroups.toArray(new Class[0]));

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
