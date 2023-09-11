package springinfra.configuration.security.idp;

import examples.repository.CredentialRepository;
import examples.service.DefaultUserDetailsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Getter
@RequiredArgsConstructor
@Configuration(DataSourceIdentityProviderModuleConfig.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = DataSourceIdentityProviderModuleConfig.BEAN_NAME)
public class DataSourceIdentityProviderModuleConfig extends BuiltInIdentityProviderConfig {
    public static final String BEAN_NAME = "dataSourceIdentityProviderModule";

    private final CredentialRepository credentialRepository;

    @Bean(DefaultUserDetailsService.BEAN_NAME)
    public UserDetailsService userDetailsService() {
        return new DefaultUserDetailsService(this.credentialRepository);
    }
}
