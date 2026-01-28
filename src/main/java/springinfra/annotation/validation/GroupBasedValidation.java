package springinfra.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springinfra.security.validator.GroupBasedValidationValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This validator validates the target object based on a validation group in the nested objects. (Since {@link jakarta.validation.Valid} doesn't support grouping)
 * Imagine objects A and B. A is a nested object that has a property of B type inside himself. Now we want to validate inside of object B
 * considering a validation group.
 * Also bear in mind that this annotation provides a way for us to be able to change the validation group conditionally at run-time. For instance,
 * it means if the condition Z is true, B should be validated as group X, and otherwise should be validated as group Y. For further information and example
 * you can take look at {@link springinfra.assets.ValidationGroups.DynamicCrudValidationGroup DynamicCrudValidationGroup} and its usage.
 */

@Constraint(validatedBy = GroupBasedValidationValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupBasedValidation {

    String message() default "Invalid DTO for operation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] value();
}
