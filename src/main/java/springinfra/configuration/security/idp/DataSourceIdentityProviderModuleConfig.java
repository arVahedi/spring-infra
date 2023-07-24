package springinfra.configuration.security.idp;

import examples.repository.CredentialRepository;
import examples.service.DefaultUserDetailsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springinfra.controller.filter.JwtValidationFilter;

@Getter
@RequiredArgsConstructor
@Configuration(DataSourceIdentityProviderModuleConfig.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = DataSourceIdentityProviderModuleConfig.BEAN_NAME)
public class DataSourceIdentityProviderModuleConfig extends BuildInIdentityProviderConfig {
    public static final String BEAN_NAME = "dataSourceIdentityProviderModule";

    private final CredentialRepository credentialRepository;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // Add JWT validator filter
        httpSecurity.addFilterBefore(
                jwtValidationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean(DefaultUserDetailsService.BEAN_NAME)
    public UserDetailsService userDetailsService() {
        return new DefaultUserDetailsService(this.credentialRepository);
    }

    @Bean
    public JwtValidationFilter jwtValidationFilter() {
        return new JwtValidationFilter();
    }
}
