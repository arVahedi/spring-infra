package org.springinfra.security.validator;

import jakarta.validation.ConstraintValidator;

import java.lang.annotation.Annotation;

/**
 * The parent of all java bean validators
 *
 * @param <A> the Constraint Validator annotation
 * @param <T> the target object for validation (usually is a general object)
 */

public abstract class BaseValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
}
