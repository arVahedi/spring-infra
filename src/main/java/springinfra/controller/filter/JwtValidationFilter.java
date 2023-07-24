package springinfra.controller.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import springinfra.configuration.security.oauth2.EnhancedJwtAuthenticationToken;
import springinfra.utility.identity.JwtUtility;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static springinfra.assets.Constant.AUTHORIZATION_TOKEN_COOKIE_NAME;
import static springinfra.assets.Constant.AUTHORIZATION_TOKEN_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private static final List<String> ALLOW_COOKIE_AUTHORIZATION_METHODS = Arrays.asList(HttpMethod.GET.name(), HttpMethod.OPTIONS.name(), HttpMethod.TRACE.name());

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get authorization header and validate
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Cookie authorizationCookie = null;
        // If the authorization token is not found in headers, we search it in cookies but only for GET or OPTION or TRACE requests
        if (StringUtils.isBlank(token) && ALLOW_COOKIE_AUTHORIZATION_METHODS.contains(request.getMethod())) {
            authorizationCookie = WebUtils.getCookie(request, AUTHORIZATION_TOKEN_COOKIE_NAME);
            token = authorizationCookie != null ? authorizationCookie.getValue() : null;
        }

        if (StringUtils.isNotBlank(token)) {
            // Get jwt token and validate
            try {
                token = token.startsWith(AUTHORIZATION_TOKEN_PREFIX) ? token.replaceFirst(AUTHORIZATION_TOKEN_PREFIX, "") : token;

                JwtUtility.validateToken(token);

                Jwt.Builder jwtBuilder = Jwt.withTokenValue(token);
                JwtUtility.getClaims(token).forEach((key, value) -> {
                    if (key.equalsIgnoreCase("exp")) {
                        jwtBuilder.expiresAt(Instant.ofEpochSecond((Long) value));
                    } else if (key.equalsIgnoreCase("iat")) {
                        jwtBuilder.issuedAt(Instant.ofEpochSecond((Long) value));
                    } else {
                        jwtBuilder.claim(key, value);
                    }
                });
                JwtUtility.getHeaders(token).forEach(jwtBuilder::header);

                // Get user identity and set it on the spring security context
                UserDetails userDetails = userDetailsService.loadUserByUsername(JwtUtility.getUsernameFromToken(token).orElseThrow());

                EnhancedJwtAuthenticationToken authentication = new EnhancedJwtAuthenticationToken(
                        jwtBuilder.build(),
                        userDetails,
                        null,
                        userDetails == null ? List.of() : userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (InvalidJwtException | UsernameNotFoundException | JoseException e) {
                log.warn("Received an invalid token {}: {}", token, e.getMessage());
                if (authorizationCookie != null) {  // We remove the cookie when it's not valid anymore
                    authorizationCookie.setMaxAge(0);
                    response.addCookie(authorizationCookie);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Autowired
    @Lazy
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
