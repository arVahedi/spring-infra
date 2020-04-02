package personal.project.springinfra.security.validator;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;

public abstract class BaseValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
}
