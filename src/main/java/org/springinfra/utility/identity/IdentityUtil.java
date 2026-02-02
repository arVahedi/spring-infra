package org.springinfra.utility.identity;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springinfra.security.oauth2.EnhancedJwtAuthenticationToken;

import java.util.Optional;

@UtilityClass
public class IdentityUtil {

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public Optional<String> getUsername() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null &&
                    authentication.getPrincipal() != null &&
                    authentication.getPrincipal() instanceof UserDetails userDetails) {
                return Optional.of(userDetails.getUsername());
            }
        }

        return Optional.empty();
    }

    public Optional<String> getUserToken() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof EnhancedJwtAuthenticationToken jwtAuthenticationToken) {
                return Optional.of(jwtAuthenticationToken.getToken().getTokenValue());
            }
        }

        return Optional.empty();
    }
}
