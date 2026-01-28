package springinfra.annotation.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springinfra.assets.AuthorityType;
import springinfra.security.validator.SecurePropertyValidator;

import java.lang.annotation.*;

/**
 * This class is implemented in order to support field-based security. It can limit any read and write operations on a specific
 * field in any dto based on the user's permissions ({@link AuthorityType})
 * <p><p>
 * {@link #accessibleFor()} defines allowed permissions to access the property
 * <p>
 * {@link #notAccessibleFor()} defines non-allowed permissions to access the property
 * <p>
 * {@link #accessibleForAnyAuthenticatedUser()} defines all authenticated users can access the property (regardless of their permissions). It can be used to define authentication as an authorization factor for accessing a property instead of considering permissions
 * <p><p>
 * <strong>Bear in mind:</strong> there is an important hierarchy between the above parameters, which means the first parameter that matches
 * the result will be returned and the other parameters will be ignored, so basically only one of them should be used at the same time.
 * The order of processing is:
 * <ul>
 *     <li>{@link #accessibleForAnyAuthenticatedUser()}</li>
 *     <li>{@link #accessibleFor()}</li>
 *     <li>{@link #notAccessibleFor()}</li>
 * </ul>
 * <p><p>
 * The write operations are limited by the input (bean) validation mechanism, so any received request that uses the field-based secure dto (which contains properties marked by this annotation)
 * will be checked that ensure the current user has proper access to secure properties or not, exactly before going to corresponding endpoint method and as a one of input validation steps as same as other
 * required validations. for further information about this step take a look at {@link SecurePropertyValidator}
 * <p>
 * The read operations are limited by aspect mechanism. For using this feature, the method in the controller should be marked with {@link springinfra.annotation.FieldLevelSecurity} annotation,
 * to indicate the return value of this method should be protected by our filed level security mechanism. In that case, the {@link springinfra.aspect.FieldLevelSecurityAspect} will be
 * wrapped around the method and tries to manipulate its return value to ensure all return properties are allowed to be read by current user. (All not-allowed properties will be eliminated from response
 * and the rest will be returned.)
 * <p>
 *
 * @see SecurePropertyValidator
 * @see springinfra.annotation.FieldLevelSecurity
 * @see springinfra.aspect.FieldLevelSecurityAspect
 */

@Documented
@Constraint(validatedBy = SecurePropertyValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureProperty {

    String message() default "Property does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    AuthorityType[] accessibleFor() default {};

    AuthorityType[] notAccessibleFor() default {};

    boolean accessibleForAnyAuthenticatedUser() default false;
}
