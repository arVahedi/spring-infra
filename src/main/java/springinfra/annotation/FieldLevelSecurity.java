package springinfra.annotation;

import java.lang.annotation.*;

/**
 * @see springinfra.annotation.validation.SecureProperty
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldLevelSecurity {

}
