package org.springinfra.system.listener;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springinfra.assets.ClaimName;
import org.springinfra.utility.identity.JwtUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springinfra.assets.Constant.AUTHORIZATION_TOKEN_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component(SuccessfulAuthenticationHandler.BEAN_NAME)
public class SuccessfulAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String BEAN_NAME = "successfulAuthenticationHandler";

    public static final String AUTH_TOKEN_REQUEST_ATTRIBUTE = "authorizationToken";
    public static final String REDIRECT_FLAG_NAME = "redirect";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("Trying to add user's id token in cookie");
        boolean shouldBeRedirect = request.getParameter(REDIRECT_FLAG_NAME) != null;

        try {
            String authToken = null;
            if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
                OidcIdToken idToken = ((DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal()).getIdToken();
                authToken = idToken.getTokenValue();
                shouldBeRedirect = true;
            } else if (authentication.getPrincipal() instanceof User user) {
                Map<String, Object> claims = new HashMap<>();
                claims.put(ClaimName.USERNAME, user.getUsername());
                log.info("Generating id token for user {}", user.getUsername());
                authToken = JwtUtility.generateToken(claims);
            }

            request.setAttribute(AUTH_TOKEN_REQUEST_ATTRIBUTE, authToken);


            response.addCookie(generateAuthorizationCookie(Optional.of(authToken)));
            log.info("Authorization cookie is set successfully");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        if (shouldBeRedirect) {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    public static Cookie generateAuthorizationCookie(Optional<String> token) {
        Cookie authCookie = new Cookie(AUTHORIZATION_TOKEN_COOKIE_NAME, token.orElse(null));
        authCookie.setPath("/");
        authCookie.setSecure(true);
        authCookie.setHttpOnly(false);
        authCookie.setAttribute("SameSite", org.springframework.boot.web.server.Cookie.SameSite.STRICT.attributeValue());
        if (token.isEmpty()) {
            authCookie.setMaxAge(0);
        }
        return authCookie;
    }
}
