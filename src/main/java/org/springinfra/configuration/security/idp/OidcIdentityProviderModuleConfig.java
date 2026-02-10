package org.springinfra.configuration.security.idp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springinfra.SpringContext;
import org.springinfra.configuration.BeanConfig;
import org.springinfra.security.oauth2.*;
import org.springinfra.system.listener.SuccessfulAuthenticationHandler;
import org.springinfra.utility.identity.JwtUtil;

import java.net.URI;
import java.util.*;

import static org.springinfra.assets.Constant.AUTHORITY_POSTFIX;

/**
 * For further information read this <a href="https://docs.spring.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-userinfo-endpoint">documentation</a>
 * <p>
 * And also bear in mind for oidc authentication, spring security uses the below classes:
 *
 * @see org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider#authenticate(Authentication) OidcAuthorizationCodeAuthenticationProvider
 * @see org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService#loadUser(OidcUserRequest) OidcUserService
 * @see org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService#loadUser(OAuth2UserRequest) DefaultOAuth2UserService.
 */

@Getter
@Slf4j
@RequiredArgsConstructor
@Configuration(OidcIdentityProviderModuleConfig.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = OidcIdentityProviderModuleConfig.BEAN_NAME)
public class OidcIdentityProviderModuleConfig extends BaseIdentityProviderModuleConfig {
    public static final String BEAN_NAME = "oidcIdentityProviderModule";

    @Value("${security.idp.oidc.provider}")
    private String oidcProvider;
    @Value("${security.idp.oidc.single-sign-out:false}")
    private boolean activateSingleSignOut;

    private final SuccessfulAuthenticationHandler successfulAuthenticationHandler;
    private final ObjectProvider<ClientRegistrationRepository> clientRegistrationRepository;
    private final ObjectProvider<BearerTokenResolver> bearerTokenResolver;
    private final ObjectProvider<JwtDecoder> jwtDecoder;
    private final ObjectProvider<Converter<Jwt, AbstractAuthenticationToken>> enhancedJwtAuthenticationConverter;

    /**
     * @param httpSecurity the global HttpSecurity retrieved from WebSecurityConfig
     * @see BaseIdentityProviderModuleConfig#configure(HttpSecurity)
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        //This line is responsible for handling post-authentication requests, which means this causes we be able to convert the authorization header to the corresponding ID token and realize whether the user is authenticated or not.
        httpSecurity.oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
                .bearerTokenResolver(this.bearerTokenResolver.getObject())
                .authenticationEntryPoint((request, response, authException) -> {
                    throw new InvalidBearerTokenException(authException.getMessage(), authException);
                })
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoderDecorator())
                        .jwtAuthenticationConverter(this.enhancedJwtAuthenticationConverter.getObject())));

        httpSecurity.oauth2Client(Customizer.withDefaults());

        httpSecurity.oauth2Login(Customizer.withDefaults());
        httpSecurity.oauth2Login(oAuth2LoginConfigurer -> oAuth2LoginConfigurer
                .loginPage("/login")    // It disables the default login page generator of Spring (DefaultLoginPageGeneratingFilter) in order to we can use our custom login page
                .successHandler(this.successfulAuthenticationHandler)
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(oidcUserService())));
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return new EnhancedBearerTokenResolver();
    }

    /**
     * This bean is registered here to be used as authentication converter for our JWT tokens which are basically our user's ID tokens.
     * When a new request is received, we use this class for converting its JWT token in the HTTP Authorization header to an Authentication object
     * in the authentication manager. This is part of our oauth2ResourceServer configuration in the {@link #configure(HttpSecurity)} method
     *
     * @return an instance of {@link EnhancedJwtAuthenticationConverter EnhancedJwtAuthenticationConverter}
     */
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        ClientRegistration clientRegistration = this.clientRegistrationRepository.getObject().findByRegistrationId(this.oidcProvider);
        EnhancedJwtAuthenticationConverter converter = new EnhancedJwtAuthenticationConverter();
        converter.setPrincipalClaimName(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
        converter.setJwtGrantedAuthoritiesConverter(oidcJwtGrantedAuthoritiesConverter());
        return converter;
    }

    /**
     * This bean automatically registers authority mapper for spring security oAuth2
     * In other words, we customize our logic for parsing user's ID token and extracting his permissions, and Spring
     * automatically will use this bean to pass the user's ID token and getting his granted authorities (permissions).
     *
     * @return GrantedAuthoritiesMapper: customized mapper for extracting user's permission from his ID token
     */
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority oidcUserAuthority) {
                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    Map<String, Object> headers = new LinkedHashMap<>();
                    try {
                        headers = JwtUtil.getHeadersWithoutValidation(idToken.getTokenValue());
                    } catch (InvalidJwtException | JoseException e) {
                        log.info(e.getMessage(), e);
                    }

                    Jwt idTokenJwt = new Jwt(idToken.getTokenValue(), idToken.getIssuedAt(), idToken.getExpiresAt(), headers, idToken.getClaims());
                    mappedAuthorities.addAll(Objects.requireNonNull(oidcJwtGrantedAuthoritiesConverter().convert(idTokenJwt)));
                }
            });

            return mappedAuthorities;
        };
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    /**
     * This is a custom LogoutHandler that handles ending sessions of OIDC in the authorization provider and also revokes the user's ID token.
     * As a result the id token wouldn't be valid (active) anymore if we do inquiry from the introspect token endpoint of the OIDC provider as well as
     * the user's session would be closed there.
     *
     * @return a custom LogoutHandler
     * @see org.springframework.security.web.authentication.logout.LogoutHandler
     */
    @Bean
    public LogoutHandler revocationTokenLogoutHandler() {
        return (request, response, authentication) -> {
            ClientRegistration clientRegistration = this.clientRegistrationRepository.getObject().findByRegistrationId(this.oidcProvider);
            Optional<URI> endSessionEndpoint = endSessionEndpoint(clientRegistration);
            if (authentication instanceof EnhancedJwtAuthenticationToken enhancedJwtAuthenticationToken && endSessionEndpoint.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
                payload.add("id_token_hint", enhancedJwtAuthenticationToken.getToken().getTokenValue());

                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(payload, headers);

                ResponseEntity<String> revocationResponse = SpringContext.getApplicationContext().getBean(BeanConfig.INTERNAL_REST_TEMPLATE_BEAN_NAME, RestTemplate.class).postForEntity(endSessionEndpoint.get(), entity, String.class);
                if (revocationResponse.getStatusCode().is2xxSuccessful()) {
                    log.info("OIDC session is closed successfully for user [{}]", ((User) authentication.getPrincipal()).getUsername());
                }
            }
        };
    }

    /**
     * This method returns all active login urls that support OIDC login (Usually we support only one (keycloak) but technically
     * it could be more (e.g: keycloak + google + facebook + any other OIDC provider)
     * <p>
     * This logic has been copied from {@link OAuth2LoginConfigurer#getLoginLinks()}
     *
     * @return a map of existing OIDC login links
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getLoginLinks() {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(this.clientRegistrationRepository.getObject()).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) this.clientRegistrationRepository.getObject();
        }
        if (clientRegistrations == null) {
            return Collections.emptyMap();
        }
        String authorizationRequestBaseUri =
                /* (this.authorizationEndpointConfig.authorizationRequestBaseUri != null) ? this.authorizationEndpointConfig.authorizationRequestBaseUri :*/
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
        Map<String, String> loginUrlToClientName = new HashMap<>();
        clientRegistrations.forEach(registration -> {
            if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(registration.getAuthorizationGrantType())) {
                String authorizationRequestUri = authorizationRequestBaseUri + "/" + registration.getRegistrationId();
                loginUrlToClientName.put(authorizationRequestUri, registration.getClientName());
            }
        });
        return loginUrlToClientName;
    }

    /**
     * Create an instance of our custom grant authorities converter for OIDC.
     * In other words, this converter is responsible for extracting the user's authorities (permissions) according to his ID token.
     *
     * @return a Converter of converting JWT token to collection of user's authorities
     * @see OidcJwtGrantedAuthoritiesConverter
     */
    private Converter<Jwt, Collection<GrantedAuthority>> oidcJwtGrantedAuthoritiesConverter() {
        ClientRegistration clientRegistration = this.clientRegistrationRepository.getObject().findByRegistrationId(this.oidcProvider);
        return new OidcJwtGrantedAuthoritiesConverter(clientRegistration.getClientId(), AUTHORITY_POSTFIX);
    }

    /**
     * This method decorates the original {@link JwtDecoder} of the Spring, to make us be able to add our custom {@link OAuth2TokenValidator} to it.
     * Since creation of this decoder is complex in the Spring, and it's done based on different configuration that are provided in properties files,
     * we don't want to create it in our side here. So we let the Spring creates its own {@link JwtDecoder} as a Bean, and then we decorate it here and set it
     * our {@link org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer} on it manually.
     * <p>
     * <strong>Bear in mind, this method SHOULD NOT define a Bean, because it prevents the Spring to create its own JwtDecoder bean.</strong>
     *
     * @return a decorated {@link JwtDecoder} with custom {@link OAuth2TokenValidator}
     */
    private JwtDecoder jwtDecoderDecorator() {
        if (this.jwtDecoder.getObject() instanceof NimbusJwtDecoder nimbusJwtDecoder) {
            List<OAuth2TokenValidator<Jwt>> extraValidators = new ArrayList<>();

            getOidcSingSignOutValidator().ifPresent(extraValidators::add);

            if (extraValidators.isEmpty()) {
                return this.jwtDecoder.getObject();
            }

            return new NimbusJwtDecoderDecorator(nimbusJwtDecoder, new DelegatingOAuth2TokenValidator<>(extraValidators));
        }

        log.warn("The default Spring JwtDecoder is not instance of NimbusJwtDecoder, so decorating logic is interrupted");
        return this.jwtDecoder.getObject();

    }

    /**
     * Retrieving address (URI) of end_session_endpoint of the OIDC provider IF it exists.
     *
     * @param clientRegistration: the client registration which is already loaded by OIDC service discovery
     * @return Optional<URI> in that case end_session_endpoint is available or Optional.empty() is it's not available
     */
    private Optional<URI> endSessionEndpoint(ClientRegistration clientRegistration) {
        if (clientRegistration != null) {
            ClientRegistration.ProviderDetails providerDetails = clientRegistration.getProviderDetails();
            Object endSessionEndpoint = providerDetails.getConfigurationMetadata().get("end_session_endpoint");
            if (endSessionEndpoint != null) {
                return Optional.of(URI.create(endSessionEndpoint.toString()));
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieving address (URI) of introspection_endpoint of the OIDC provider IF it exists.
     *
     * @param clientRegistration: the client registration which is already loaded by OIDC service discovery
     * @return Optional<URI> in that case introspection_endpoint is available or Optional.empty() is it's not available
     */
    private Optional<URI> introspectionEndpoint(ClientRegistration clientRegistration) {
        if (clientRegistration != null) {
            ClientRegistration.ProviderDetails providerDetails = clientRegistration.getProviderDetails();
            Object endSessionEndpoint = providerDetails.getConfigurationMetadata().get("introspection_endpoint");
            if (endSessionEndpoint != null) {
                return Optional.of(URI.create(endSessionEndpoint.toString()));
            }
        }
        return Optional.empty();
    }

    private Optional<OAuth2TokenValidator<Jwt>> getOidcSingSignOutValidator() {
        OAuth2TokenValidator<Jwt> result = null;
        if (this.activateSingleSignOut) {
            ClientRegistration clientRegistration = this.clientRegistrationRepository.getObject().findByRegistrationId(this.oidcProvider);
            Optional<URI> introspectionEndpoint = introspectionEndpoint(clientRegistration);
            if (introspectionEndpoint.isPresent()) {
                result = new OidcCloudSessionValidator(introspectionEndpoint.get(), clientRegistration.getClientId(), clientRegistration.getClientSecret());
            } else {
                log.warn("Single Sign-Out is active but the authorization server doesn't support introspection_endpoint according to its OIDC Discovery");
            }
        }
        return Optional.ofNullable(result);
    }
}
