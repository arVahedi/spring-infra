package springinfra.configuration.security.idp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import springinfra.assets.AuthorityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@Configuration(InMemoryIdentityProviderModuleConfig.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = InMemoryIdentityProviderModuleConfig.BEAN_NAME)
@ConfigurationProperties(prefix = "security.idp.in-memory")
public class InMemoryIdentityProviderModuleConfig extends BuildInIdentityProviderConfig {
    public static final String BEAN_NAME = "inMemoryIdentityProviderModule";

    private List<String> credentials;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        List<UserDetails> userDetails = new ArrayList<>();
        credentials.forEach(item -> {
            String[] credentialValues = item.split(";");
            if (credentialValues.length < 2) {
                throw new IllegalArgumentException("in-memory credential should contain 2 elements (username and password) at least.");
            }

            List<String> authorities = Arrays.stream(Arrays.copyOfRange(credentialValues, 2, credentialValues.length)).map(authority -> AuthorityType.valueOf(authority).getValue()).toList();

            log.info("Register in-memory user [{}] with authorities {}", credentialValues[0], authorities);
            userDetails.add(new org.springframework.security.core.userdetails.User(credentialValues[0],
                    credentialValues[1],
                    true,
                    true,
                    true,
                    true,
                    authorities.stream().map(SimpleGrantedAuthority::new).toList()));
        });
        return new InMemoryUserDetailsManager(userDetails);
    }
}
