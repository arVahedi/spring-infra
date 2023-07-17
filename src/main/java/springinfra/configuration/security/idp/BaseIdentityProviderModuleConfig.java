package springinfra.configuration.security.idp;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import springinfra.configuration.BaseConfig;

import java.util.Optional;

@Order(99)
public abstract class BaseIdentityProviderModuleConfig implements BaseConfig {

    public void configure(HttpSecurity httpSecurity) throws Exception {

    }

    public Optional<AuthenticationManager> getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return Optional.empty();
    }
}
