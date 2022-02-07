package springinfra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import springinfra.assets.AuthorityType;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements BaseConfig {

    @Override
    public void configure(HttpSecurity http) throws Exception {

//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.csrf().disable();

//        http.headers().contentSecurityPolicy("script-src 'self'; object-src 'self'; report-uri /csp-report-endpoint/");
        //This line is required for loading swagger because it uses inline scripts
        http.headers().addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(new AntPathRequestMatcher("/doc/api/**"), new ContentSecurityPolicyHeaderWriter("script-src 'self' 'unsafe-inline'; object-src 'self'; report-uri /csp-report-endpoint/")));
        //We prevent inline scripts for all requests except these are started with /doc/api/** (swagger page)
        http.headers().addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(new RegexRequestMatcher("^(?!\\/doc\\/api\\/).+", null), new ContentSecurityPolicyHeaderWriter("script-src 'self'; object-src 'self'; report-uri /csp-report-endpoint/")));

        http.authorizeRequests().antMatchers("/user/**").authenticated()
                .and()
                .authorizeRequests().antMatchers("/admin/**").authenticated()
                .and()
                .formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password")
                .loginProcessingUrl("/authenticate").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/401")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles(AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue())
                .and()
                .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles(AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue())
                .and()
                .withUser("admin").password(passwordEncoder().encode("adminPass")).roles(AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue(), AuthorityType.USER_MANAGEMENT_AUTHORITY.getValue());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }
}
