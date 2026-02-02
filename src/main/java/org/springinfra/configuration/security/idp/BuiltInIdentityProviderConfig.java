package org.springinfra.configuration.security.idp;

import lombok.Setter;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springinfra.assets.ClaimName;
import org.springinfra.security.oauth2.EnhancedBearerTokenResolver;
import org.springinfra.security.oauth2.EnhancedJwtAuthenticationConverter;
import org.springinfra.utility.identity.JwtUtil;

import java.time.Instant;

@Setter(onMethod = @__({@Autowired, @Lazy}))
public abstract class BuiltInIdentityProviderConfig extends BaseIdentityProviderModuleConfig {

    private BearerTokenResolver bearerTokenResolver;
    private JwtDecoder jwtDecoder;
    private Converter<Jwt, AbstractAuthenticationToken> enhancedJwtAuthenticationConverter;
    private UserDetailsService userDetailsService;

    /**
     * @param httpSecurity the global HttpSecurity retrieved from WebSecurityConfig
     * @see BaseIdentityProviderModuleConfig#configure(HttpSecurity)
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        // We handle form login in our restful endpoint (AuthenticationController.class)
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);

        //This line is responsible for handling post-authentication requests, which means this causes we are able to convert the authorization header to the corresponding ID token and realize whether the user is authenticated or not.
        httpSecurity.oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
                .bearerTokenResolver(this.bearerTokenResolver)
                .authenticationEntryPoint((request, response, authException) -> {
                    throw new InvalidBearerTokenException(authException.getMessage(), authException);
                })
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(this.jwtDecoder)
                        .jwtAuthenticationConverter(this.enhancedJwtAuthenticationConverter)));
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return new EnhancedBearerTokenResolver();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                JwtUtil.validateToken(token);

                Jwt.Builder jwtBuilder = Jwt.withTokenValue(token);
                JwtUtil.getClaims(token).forEach((key, value) -> {
                    if (key.equalsIgnoreCase("exp")) {
                        jwtBuilder.expiresAt(Instant.ofEpochSecond((Long) value));
                    } else if (key.equalsIgnoreCase("iat")) {
                        jwtBuilder.issuedAt(Instant.ofEpochSecond((Long) value));
                    } else {
                        jwtBuilder.claim(key, value);
                    }
                });
                JwtUtil.getHeaders(token).forEach(jwtBuilder::header);

                return jwtBuilder.build();

            } catch (InvalidJwtException | JoseException e) {
                throw new JwtException(e.getMessage(), e);
            }
        };
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        EnhancedJwtAuthenticationConverter converter = new EnhancedJwtAuthenticationConverter();
        converter.setPrincipalClaimName(ClaimName.USERNAME);
        converter.setUserDetailsService(this.userDetailsService);
        return converter;
    }

    public abstract UserDetailsService userDetailsService();
}
