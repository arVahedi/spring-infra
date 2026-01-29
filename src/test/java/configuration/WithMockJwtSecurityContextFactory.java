package configuration;

import annotation.WithMockJwt;
import lombok.val;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;
import org.springinfra.security.oauth2.EnhancedJwtAuthenticationToken;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwt withUser) {
        val jwt = Jwt.withTokenValue(withUser.token())
                .header("alg", "none")
                .claim("sub", withUser.value())
                .claim("user", Map.of("username", withUser.username()))
                .build();

        val grantedAuthorities = AuthorityUtils.createAuthorityList(withUser.authorities());
        if (grantedAuthorities.isEmpty()) {
            for (String role : withUser.roles()) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> MessageFormat.format("roles cannot start with ROLE_ [role: {0}] ", role));
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        } else if (!(withUser.roles().length == 1 && "USER".equals(withUser.roles()[0]))) {
            throw new IllegalStateException("You cannot define roles attribute " + Arrays.asList(withUser.roles())
                    + " with authorities attribute " + Arrays.asList(withUser.authorities()));
        }

        User principal = new User(withUser.username(), "", grantedAuthorities);

        val token = new EnhancedJwtAuthenticationToken(jwt, principal, "", grantedAuthorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);

        return context;
    }
}
