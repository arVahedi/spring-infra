package springinfra.configuration.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;

public class EnhancedJwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {
    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public EnhancedJwtAuthenticationToken(Jwt jwt) {
        super(jwt);
    }

    public EnhancedJwtAuthenticationToken(Jwt jwt, Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(jwt, principal, credentials, authorities);
        this.setAuthenticated(true);
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return this.getToken().getClaims();
    }
}
