package springinfra.configuration.security.idp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;
import springinfra.assets.AuthorityType;

@RequiredArgsConstructor
@Component(InMemoryIdentityProviderModule.BEAN_NAME)
@ConditionalOnProperty(value = "security.idp.module", havingValue = InMemoryIdentityProviderModule.BEAN_NAME)
public class InMemoryIdentityProviderModule extends BaseIdentityProviderModule {
    public static final String BEAN_NAME = "inMemoryIdentityProviderModule";

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(new Argon2PasswordEncoder().encode("user1Pass")).roles(AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue())
                .and()
                .withUser("user2").password(new Argon2PasswordEncoder().encode("user2Pass")).roles(AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue())
                .and()
                .withUser("admin").password(new Argon2PasswordEncoder().encode("adminPass")).roles(AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue(), AuthorityType.USER_MANAGEMENT_AUTHORITY.getValue());
    }
}
