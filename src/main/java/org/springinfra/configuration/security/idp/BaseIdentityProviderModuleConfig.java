package org.springinfra.configuration.security.idp;

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

    /**
     * This method is used for customizing logout configuration based on the identity module if needed.
     * <strong>We don't use {@link HttpSecurity#logout(Customizer) logout} method in the HttpSecurity because it causes
     * the {@link org.springframework.security.web.authentication.logout.LogoutFilter LogoutFilter} be put before all authentication filters
     * which causes we don't have access to the Authentication object in our LogoutHandlers and LogoutSuccessfulHandlers.</strong>
     * To fix this issue we have to disable the logout feature of HttpSecurity and then configure the logout logic manually. (For further information take a look at {@link WebSecurityConfig#createLogoutFilter(HttpSecurity)}
     * So we use this method for doing any identity module-base customization on the logoutConfigurer.
     *
     * @param logoutConfigurer The LogoutConfigurer object which would be used for creating LogoutFilter
     */
    public <H extends HttpSecurityBuilder<H>> void configureLogout(LogoutConfigurer<H> logoutConfigurer) {
    }
}
