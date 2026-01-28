package springinfra.security.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static springinfra.assets.Constant.AUTHORIZATION_TOKEN_COOKIE_NAME;
import static springinfra.assets.Constant.AUTHORIZATION_TOKEN_PREFIX;

/**
 * This class is a Decorator of Spring {@link org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver}
 * that adds support for {@link org.springframework.http.HttpHeaders#AUTHORIZATION} header in cookie.
 */

@Getter
@Setter
public class EnhancedBearerTokenResolver implements BearerTokenResolver {

    private static final List<String> ALLOW_COOKIE_AUTHORIZATION_METHODS = Arrays.asList(HttpMethod.GET.name(), HttpMethod.OPTIONS.name(), HttpMethod.TRACE.name());
    private DefaultBearerTokenResolver defaultBearerTokenResolver;

    public EnhancedBearerTokenResolver() {
        this.defaultBearerTokenResolver = new DefaultBearerTokenResolver();
    }

    @Override
    public String resolve(HttpServletRequest request) {
        String token = this.defaultBearerTokenResolver.resolve(request);

        if (StringUtils.isBlank(token) && ALLOW_COOKIE_AUTHORIZATION_METHODS.contains(request.getMethod())) {
            Cookie authorizationCookie = WebUtils.getCookie(request, AUTHORIZATION_TOKEN_COOKIE_NAME);
            token = authorizationCookie != null ? authorizationCookie.getValue() : null;
            if (StringUtils.isNotBlank(token) && org.springframework.util.StringUtils.startsWithIgnoreCase(token, AUTHORIZATION_TOKEN_PREFIX)) {
                token = token.replaceAll("(?i)" + Pattern.quote(AUTHORIZATION_TOKEN_PREFIX), "");
            }
        }

        return token;
    }
}
