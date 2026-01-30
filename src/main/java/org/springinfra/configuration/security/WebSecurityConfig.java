package org.springinfra.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.*;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springinfra.configuration.BaseConfig;
import org.springinfra.configuration.security.idp.BaseIdentityProviderModuleConfig;
import org.springinfra.configuration.security.idp.BuiltInIdentityProviderConfig;
import org.springinfra.configuration.security.idp.OidcIdentityProviderModuleConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.springinfra.assets.AuthorityType.StringFormat.MONITORING_AUTHORITY;
import static org.springinfra.assets.Constant.CSP_REPOST_ENDPOINT;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class WebSecurityConfig implements BaseConfig {

    private final Optional<BaseIdentityProviderModuleConfig> identityProviderModuleConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        configureSecurityHeaders(httpSecurity);

        httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // No session will be created or used by Spring Security.

        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
//                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(MONITORING_AUTHORITY)        // For accessing to all actuator endpoints this specific authority will be needed (if management.endpoints.web.base-path is set to /monitoring rather than default (/actuator))
                .anyRequest().permitAll());     // We permit all requests here to check those required authorities via prePostAnnotation on the class or method level. If we remove this line, those annotations won't work and all requests will be blocked regardless of their authorities

        // We disabled the default logout filter because we want to change its order to be after the authentication filter to be able to access the Authentication object in our LogoutSuccessfulHandler(s)
        httpSecurity
                .logout(AbstractHttpConfigurer::disable)
                .addFilterAfter(createLogoutFilter(httpSecurity), AuthorizationFilter.class);


        this.identityProviderModuleConfig.ifPresent(baseIdentityProviderModuleConfig -> baseIdentityProviderModuleConfig.configure(httpSecurity));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    /**
     * Exposing a global AuthenticationManager as a bean due to be able to use it in all over the application
     * such as our custom login rest endpoint in our build-in identity provider.
     * <strong>This bean creation is conditional on existing a {@link BuiltInIdentityProviderConfig} because
     * exposing AuthenticationManager as a bean with OAuth2 ({@link OidcIdentityProviderModuleConfig})
     * can cause stack overflow issue when authentication is rejected</strong>
     *
     * @param authenticationConfiguration global configuration of authentication manager that is used for getting default authentication manager
     *                                    in case that we don't have any custom authentication manager in our identity provider module ({@link BaseIdentityProviderModuleConfig#getAuthenticationManager(AuthenticationConfiguration)})
     * @return an AuthenticationManager as a global bean
     */
    @Bean
    @ConditionalOnBean(BuiltInIdentityProviderConfig.class)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        Optional<AuthenticationManager> authenticationManager = Optional.empty();
        if (this.identityProviderModuleConfig.isPresent()) {
            authenticationManager = this.identityProviderModuleConfig.get().getAuthenticationManager(authenticationConfiguration);
        }

        return authenticationManager.orElse(authenticationConfiguration.getAuthenticationManager());
    }

    /**
     * We don't use {@link HttpSecurity#logout(Customizer) logout} method in the HttpSecurity because it causes
     * the {@link org.springframework.security.web.authentication.logout.LogoutFilter LogoutFilter} be put before all authentication filters
     * which causes we don't have access to the Authentication object in our LogoutHandlers and LogoutSuccessfulHandlers.
     * To fix this issue we have to disable the logout feature of HttpSecurity and then configure the logout logic manually.
     *
     * @param httpSecurity the global object of HttpSecurity that is used for all security configuration
     * @return corresponding our customized LogoutFilter
     * @throws NoSuchMethodException     a reflection based exception when we try to invoke this private {@link LogoutConfigurer#createLogoutFilter(HttpSecurityBuilder)} method for creating LogoutFilter
     * @throws InvocationTargetException a reflection based exception when we try to invoke this private {@link LogoutConfigurer#createLogoutFilter(HttpSecurityBuilder)} method for creating LogoutFilter
     * @throws IllegalAccessException    a reflection based exception when we try to invoke this private {@link LogoutConfigurer#createLogoutFilter(HttpSecurityBuilder)} method for creating LogoutFilter
     */
    private LogoutFilter createLogoutFilter(HttpSecurity httpSecurity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL));

        LogoutConfigurer<HttpSecurity> logoutConfigurer = new LogoutConfigurer<>();
        logoutConfigurer.setBuilder(httpSecurity);
        logoutConfigurer.invalidateHttpSession(true);
        logoutConfigurer.logoutUrl("/logout");
        logoutConfigurer.logoutSuccessUrl("/");
        logoutConfigurer.addLogoutHandler(clearSiteData);

        // Ask identityProviderModuleConfig for customized logout logic
        this.identityProviderModuleConfig.ifPresent(identityProviderModule -> identityProviderModule.configureLogout(logoutConfigurer));

        Method createLogoutFilterMethod = logoutConfigurer.getClass().getDeclaredMethod("createLogoutFilter", HttpSecurityBuilder.class);
        createLogoutFilterMethod.setAccessible(true);
        LogoutFilter logoutFilter = (LogoutFilter) createLogoutFilterMethod.invoke(logoutConfigurer, httpSecurity);
        createLogoutFilterMethod.setAccessible(false);

        return logoutFilter;
    }

    /**
     * This method configures the HTTP headers that are important security-wise
     *
     * @param httpSecurity The global shared http security object
     */
    private void configureSecurityHeaders(HttpSecurity httpSecurity) {
        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .crossOriginOpenerPolicy(crossOriginOpenerPolicyConfig -> crossOriginOpenerPolicyConfig.policy(CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN)));

        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .crossOriginEmbedderPolicy(crossOriginEmbedderPolicyConfig -> crossOriginEmbedderPolicyConfig.policy(CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP)));

        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .crossOriginResourcePolicy(crossOriginResourcePolicyConfig -> crossOriginResourcePolicyConfig.policy(CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_SITE)));

        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)));

        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.addHeaderWriter((request, response) -> response.setHeader("X-Permitted-Cross-Domain-Policies", "none")));

//        httpSecurity.headers().contentSecurityPolicy("script-src 'self'; object-src 'self'; report-uri /csp-report-endpoint/");
        //This line is required for loading swagger because it uses inline scripts
        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(PathPatternRequestMatcher.withDefaults().matcher("/doc/api/**"), new ContentSecurityPolicyHeaderWriter("script-src 'self' 'unsafe-inline'; object-src 'self'; report-uri " + CSP_REPOST_ENDPOINT))));
        //We prevent inline scripts for all requests except these are started with /doc/api/** (swagger page)
        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(new RegexRequestMatcher("^(?!\\/doc\\/api\\/).+", null), new ContentSecurityPolicyHeaderWriter("script-src 'self'; object-src 'self'; report-uri " + CSP_REPOST_ENDPOINT))));

    }
}
