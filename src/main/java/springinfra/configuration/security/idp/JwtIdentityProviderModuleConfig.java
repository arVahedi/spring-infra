package springinfra.configuration.security.idp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import springinfra.SpringContext;

@Getter
@RequiredArgsConstructor
@Configuration(JwtIdentityProviderModuleConfig.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = JwtIdentityProviderModuleConfig.BEAN_NAME)
public class JwtIdentityProviderModuleConfig extends BaseIdentityProviderModuleConfig {
    public static final String BEAN_NAME = "jwtIdentityProviderModule";

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(SpringContext.getApplicationContext().getBean(PasswordEncoder.class));
    }
}
