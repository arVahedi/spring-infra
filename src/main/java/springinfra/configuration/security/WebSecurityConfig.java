package springinfra.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import springinfra.configuration.BaseConfig;
import springinfra.configuration.security.idp.BaseIdentityProviderModuleConfig;
import springinfra.configuration.security.idp.BuildInIdentityProviderConfig;

import java.util.Optional;

import static springinfra.assets.Constant.AUTHORIZATION_TOKEN_COOKIE_NAME;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig implements BaseConfig {

    private final Optional<BaseIdentityProviderModuleConfig> identityProviderModuleConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

//        httpSecurity.headers().contentSecurityPolicy("script-src 'self'; object-src 'self'; report-uri /csp-report-endpoint/");
        //This line is required for loading swagger because it uses inline scripts
        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(new AntPathRequestMatcher("/doc/api/**"), new ContentSecurityPolicyHeaderWriter("script-src 'self' 'unsafe-inline'; object-src 'self'; report-uri /csp-report-endpoint/"))));
        //We prevent inline scripts for all requests except these are started with /doc/api/** (swagger page)
        httpSecurity.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(new RegexRequestMatcher("^(?!\\/doc\\/api\\/).+", null), new ContentSecurityPolicyHeaderWriter("script-src 'self'; object-src 'self'; report-uri /csp-report-endpoint/"))));

        httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // No session will be created or used by Spring Security.

        httpSecurity
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies(AUTHORIZATION_TOKEN_COOKIE_NAME, "JSESSIONID").invalidateHttpSession(true));

        if (this.identityProviderModuleConfig.isPresent()) {
            this.identityProviderModuleConfig.get().configure(httpSecurity);
        }

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    // Because exposing AuthenticationManager as a bean with OAuth2 can cause stack overflow issue when authentication is rejected
    @ConditionalOnBean(BuildInIdentityProviderConfig.class)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        Optional<AuthenticationManager> authenticationManager = Optional.empty();
        if (this.identityProviderModuleConfig.isPresent()) {
            authenticationManager = this.identityProviderModuleConfig.get().getAuthenticationManager(authenticationConfiguration);
        }

        return authenticationManager.orElse(authenticationConfiguration.getAuthenticationManager());
    }
}
