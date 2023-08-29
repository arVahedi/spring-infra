package springinfra.configuration.security.idp;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import springinfra.assets.ClaimName;
import springinfra.security.oauth2.EnhancedBearerTokenResolver;
import springinfra.security.oauth2.EnhancedJwtAuthenticationConverter;
import springinfra.utility.identity.JwtUtility;

import java.time.Instant;

public abstract class BuildInIdentityProviderConfig extends BaseIdentityProviderModuleConfig {

    /**
     * @param httpSecurity the global HttpSecurity retrieved from WebSecurityConfig
     * @throws Exception any expected exception
     * @see BaseIdentityProviderModuleConfig#configure(HttpSecurity)
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        //This line is responsible for handling post-authentication requests, which means this causes we be able to convert the authorization header to the corresponding ID token and realize whether the user is authenticated or not.
        httpSecurity.oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
                .bearerTokenResolver(new EnhancedBearerTokenResolver())
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));
    }

    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                JwtUtility.validateToken(token);

                Jwt.Builder jwtBuilder = Jwt.withTokenValue(token);
                JwtUtility.getClaims(token).forEach((key, value) -> {
                    if (key.equalsIgnoreCase("exp")) {
                        jwtBuilder.expiresAt(Instant.ofEpochSecond((Long) value));
                    } else if (key.equalsIgnoreCase("iat")) {
                        jwtBuilder.issuedAt(Instant.ofEpochSecond((Long) value));
                    } else {
                        jwtBuilder.claim(key, value);
                    }
                });
                JwtUtility.getHeaders(token).forEach(jwtBuilder::header);

                return jwtBuilder.build();

            } catch (InvalidJwtException | JoseException e) {
                throw new JwtException(e.getMessage(), e);
            }
        };
    }

    public EnhancedJwtAuthenticationConverter jwtAuthenticationConverter() {
        EnhancedJwtAuthenticationConverter converter = new EnhancedJwtAuthenticationConverter();
        converter.setPrincipalClaimName(ClaimName.USERNAME);
        converter.setUserDetailsService(userDetailsService());
        return converter;
    }

    public abstract UserDetailsService userDetailsService();
}
