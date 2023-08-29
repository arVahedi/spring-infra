package springinfra.security.oauth2;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used for gathering the user's authorities (permissions) according to his ID token.
 * In simplest words, it gets the user's OIDC ID token (which is always JWT) and returns his permissions back.
 */
@NoArgsConstructor
public class OidcJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private String clientId;
    private String authorityPostfix;

    public OidcJwtGrantedAuthoritiesConverter(String clientId, String authorityPostfix) {
        this.clientId = clientId;
        this.authorityPostfix = authorityPostfix;
    }

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(jwt)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

    private Set<String> getAuthorities(Jwt jwt) {
        Set<String> authorities = Stream.concat(getRealmRoles(jwt).stream(), getResourceRoles(jwt).stream()).collect(Collectors.toSet());
        if (StringUtils.isNotBlank(authorityPostfix)) {
            authorities = authorities.stream().filter(item -> item.endsWith(authorityPostfix)).collect(Collectors.toSet());
        }

        return authorities;
    }

    private List<String> getRealmRoles(Jwt jwt) {
        List<String> authorities = new ArrayList<>();
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> realmsRoles;
        if (realmAccess != null && (realmsRoles = (List<String>) realmAccess.get("roles")) != null) {
            authorities.addAll(realmsRoles);
        }

        return authorities;
    }

    private List<String> getResourceRoles(Jwt jwt) {
        List<String> authorities = new ArrayList<>();
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> clientResource;
        Collection<String> clientRoles;
        if (resourceAccess != null
                && (clientResource = (Map<String, Object>) resourceAccess.get(clientId)) != null
                && (clientRoles = (Collection<String>) clientResource.get("roles")) != null) {
            authorities.addAll(clientRoles);
        }

        return authorities;
    }
}
