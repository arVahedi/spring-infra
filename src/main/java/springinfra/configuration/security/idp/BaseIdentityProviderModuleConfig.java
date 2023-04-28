package springinfra.configuration.security.idp;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import springinfra.configuration.BaseConfig;

public abstract class BaseIdentityProviderModuleConfig implements BaseConfig {

    public abstract void configure(AuthenticationManagerBuilder auth) throws Exception;
}
