package org.springinfra.annotation.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springinfra.security.validator.ValidateAsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidateAsValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAs {

    String message() default "";

    Class<? extends Payload>[] payload() default {};

    String value();

    Class<?>[] groups() default {};
}
