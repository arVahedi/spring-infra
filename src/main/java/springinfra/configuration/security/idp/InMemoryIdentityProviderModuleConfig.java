package springinfra.configuration.security.idp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import springinfra.assets.AuthorityType;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration(InMemoryIdentityProviderModuleConfig.BEAN_NAME)
@ConfigurationProperties(prefix = "security.idp.in-memory")
@ConditionalOnProperty(value = "security.idp.module", havingValue = InMemoryIdentityProviderModuleConfig.BEAN_NAME)
public class InMemoryIdentityProviderModuleConfig extends BaseIdentityProviderModuleConfig {
    public static final String BEAN_NAME = "inMemoryIdentityProviderModule";

    private List<String> credentials;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryUserDetailsManagerConfigurer = auth.inMemoryAuthentication();

        credentials.forEach(item -> {
            String[] credentialValues = item.split(";");
            if (credentialValues.length < 2) {
                throw new IllegalArgumentException("in-memory credential should contain 2 elements (username and password) at least.");
            }

            String[] authorities = Arrays.stream(Arrays.copyOfRange(credentialValues, 2, credentialValues.length)).map(authority -> AuthorityType.valueOf(authority).getValue()).toArray(String[]::new);
            
            inMemoryUserDetailsManagerConfigurer.withUser(credentialValues[0]).password(credentialValues[1]).authorities(authorities);
        });
    }
}
