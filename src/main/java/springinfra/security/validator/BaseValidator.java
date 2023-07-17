package springinfra.security.validator;

import jakarta.validation.ConstraintValidator;
import java.lang.annotation.Annotation;

public abstract class BaseValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
}
