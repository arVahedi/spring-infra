package configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

@TestConfiguration
public class MockIdentityProviderConfig {

    @Bean
    @Primary
    public BearerTokenResolver mockBearerTokenResolver() {
        return request -> null;
    }
}
