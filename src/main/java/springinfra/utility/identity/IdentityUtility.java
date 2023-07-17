package springinfra.utility.identity;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@UtilityClass
public class IdentityUtility {

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public Optional<String> getUsername() {
        if (isAuthenticated()) {
            return Optional.ofNullable(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        }

        return Optional.empty();
    }
}
