package springinfra.configuration.security.idp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import springinfra.configuration.security.oauth2.EnhancedJwtAuthenticationConverter;
import springinfra.configuration.security.oauth2.OAuth2JwtGrantedAuthoritiesConverter;

import static springinfra.assets.Constant.AUTHORITY_POSTFIX;

@Getter
@RequiredArgsConstructor
@Configuration(OAuth2IdentityProviderModuleConfig.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = OAuth2IdentityProviderModuleConfig.BEAN_NAME)
public class OAuth2IdentityProviderModuleConfig extends BaseIdentityProviderModuleConfig {
    public static final String BEAN_NAME = "oAuth2IdentityProviderModule";

    @Value("${security.idp.oauth2.resourceserver.client-id}")
    private String clientId;
    @Value("${security.oauth2.jwt.converter.principal-attribute}")
    private String tokenPrincipalAttribute;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())));
    }

    @Bean
    public EnhancedJwtAuthenticationConverter jwtAuthenticationConverter() {
        EnhancedJwtAuthenticationConverter converter = new EnhancedJwtAuthenticationConverter();
        converter.setPrincipalClaimName(tokenPrincipalAttribute);
        converter.setJwtGrantedAuthoritiesConverter(new OAuth2JwtGrantedAuthoritiesConverter(clientId, AUTHORITY_POSTFIX));
        return converter;
    }

    // Just in case, if we want to set up our custom JWT decoder and validator
    /*@Bean
    public JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> myCustomJwtValidator = new MyImplementationOfOAuth2TokenValidator();
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(myPublicKey).build();
        jwtDecoder.setJwtValidator(myCustomJwtValidator);
        return jwtDecoder;
    }*/
}
