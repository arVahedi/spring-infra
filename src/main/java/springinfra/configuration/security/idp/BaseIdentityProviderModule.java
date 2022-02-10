package springinfra.configuration.security.idp;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public abstract class BaseIdentityProviderModule {

    public abstract void configure(AuthenticationManagerBuilder auth) throws Exception;
}
