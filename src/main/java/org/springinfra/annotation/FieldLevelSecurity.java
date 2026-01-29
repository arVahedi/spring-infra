package org.springinfra.annotation;

import org.springinfra.annotation.validation.SecureProperty;

import java.lang.annotation.*;

/**
 * @see SecureProperty
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldLevelSecurity {

}
