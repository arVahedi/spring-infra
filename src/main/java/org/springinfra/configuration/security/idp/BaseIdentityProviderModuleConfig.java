package org.springinfra.configuration.security.idp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springinfra.configuration.BaseConfig;
import org.springinfra.configuration.security.WebSecurityConfig;

import java.util.Optional;

/**
 * This is a parent class for any identity provider module that we have. By this abstraction we can customize our configuration of HttpSecurity
 * and all other required objects based on the different identity providers that we have.
 *
 * @see WebSecurityConfig
 */
@Slf4j
@Order(99)
public abstract class BaseIdentityProviderModuleConfig implements BaseConfig {

    /**
     * For doing any custom configuration on HttpSecurity which is specifically related to a particular identity module
     *
     * @param httpSecurity the global HttpSecurity retrieved from WebSecurityConfig
     */
    public void configure(HttpSecurity httpSecurity) {

    }

    /**
     * This method is used for exposing a customized AuthenticationManager as a bean if it's needed. It's usually used
     * by {@link WebSecurityConfig#authenticationManager(AuthenticationConfiguration) this method}
     * in the global security configuration
     *
     * @param authenticationConfiguration global configuration of authentication
     * @return Optional a customized authentication manager
     */
    public Optional<AuthenticationManager> getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return Optional.empty();
    }
}
