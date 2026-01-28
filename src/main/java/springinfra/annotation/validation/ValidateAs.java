package springinfra.annotation.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springinfra.security.validator.ValidateAsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidateAsValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAs {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    Class<?>[] withGroups() default {};
}
