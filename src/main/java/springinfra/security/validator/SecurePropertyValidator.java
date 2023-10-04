package springinfra.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import springinfra.annotation.validation.SecureProperty;
import springinfra.assets.AuthorityType;
import springinfra.utility.identity.IdentityUtility;

import java.util.List;

/**
 * @see SecureProperty
 */

@Slf4j
@RequiredArgsConstructor
public class SecurePropertyValidator implements ConstraintValidator<SecureProperty, Object> {

    private SecureProperty constraintAnnotation;

    public static boolean isAccessible(SecureProperty secureProperty) {
        if (secureProperty.accessibleForAnyAuthenticatedUser()) {
            return IdentityUtility.isAuthenticated();
        }

        if (!IdentityUtility.isAuthenticated()) {
            return false;
        }

        List<String> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).map(String::toLowerCase).toList();

        for (AuthorityType allowedAuthority : secureProperty.accessibleFor()) {
            if (userAuthorities.contains(allowedAuthority.getValue().toLowerCase())) {
                return true;
            }
        }

        for (AuthorityType deniedAuthority : secureProperty.notAccessibleFor()) {
            if (userAuthorities.contains(deniedAuthority.getValue().toLowerCase())) {
                return false;
            }
        }

        return false;
    }

    @Override
    public void initialize(SecureProperty constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return isAccessible(constraintAnnotation);
    }
}
