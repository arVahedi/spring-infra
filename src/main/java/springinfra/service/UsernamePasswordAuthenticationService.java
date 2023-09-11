package springinfra.service;

import examples.dto.AuthRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import springinfra.configuration.security.idp.BuiltInIdentityProviderConfig;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.Optional;

/**
 * This service class is used for authenticating users via their username and password. It's usually used by {@link springinfra.controller.rest.AuthenticationController}
 * for supporting a <strong>built-in</strong> authentication mechanism.
 * <p>
 * Consider that the word built-in means we don't use any external authorization server like what we use in the OIDC protocol. That's the reason
 * why creation of this bean depends on existing an instance of {@link BuiltInIdentityProviderConfig}, because just in that case we are supporting
 * a built-in authentication mechanism and need to this service, otherwise we are likely using OIDC protocol and an external authorization server.
 */

@Slf4j
@Service
@ConditionalOnBean(BuiltInIdentityProviderConfig.class)
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationService extends BaseService {

    private final Optional<AuthenticationManager> authenticationManager;
    private final Optional<SessionAuthenticationStrategy> sessionStrategy;
    private final Optional<SecurityContextRepository> securityContextRepository;
    private final Optional<AuthenticationSuccessHandler> successHandler;
    private final Optional<AuthenticationFailureHandler> failureHandler;
    private final Optional<SecurityContextHolderStrategy> securityContextHolderStrategy;
    private final Optional<ApplicationEventPublisher> eventPublisher;
    private final Optional<RememberMeServices> rememberMeServices;

    private final SessionAuthenticationStrategy defaultSessionStrategy = new NullAuthenticatedSessionStrategy();
    private final SecurityContextRepository defaultSecurityContextRepository = new RequestAttributeSecurityContextRepository();
    private final AuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private final AuthenticationFailureHandler defaultFailureHandler = new SimpleUrlAuthenticationFailureHandler();
    private final SecurityContextHolderStrategy defaultSecurityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final RememberMeServices defaultRememberMeServices = new NullRememberMeServices();

    public Authentication authenticate(HttpServletRequest request, HttpServletResponse response, AuthRequest authRequest) throws OperationNotSupportedException, ServletException, IOException {
        try {
            if (authenticationManager.isEmpty()) {     // If the AuthenticationManager is not exposed globally as a bean, means we don't support build-in authentication on this server. (likely because we are using oAuth2)
                throw new OperationNotSupportedException("Build-in Authentication is not supported. 410 http status would be returned to the client.");
            }

            Authentication authentication = this.authenticationManager.get().authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            log.info("User [{}] has been authenticated successfully", authRequest.getUsername());

            this.sessionStrategy.orElse(this.defaultSessionStrategy).onAuthentication(authentication, request, response);
            successfulAuthentication(request, response, authentication);

            return authentication;
        } catch (InternalAuthenticationServiceException ex) {
            log.error("An internal error occurred while trying to authenticate the user.", ex);
            unsuccessfulAuthentication(request, response, ex);
            throw ex;
        } catch (AuthenticationException ex) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, ex);
            throw ex;
        }
    }


    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = this.securityContextHolderStrategy.orElse(this.defaultSecurityContextHolderStrategy).createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.orElse(this.defaultSecurityContextHolderStrategy).setContext(context);
        this.securityContextRepository.orElse(this.defaultSecurityContextRepository).saveContext(context, request, response);

        log.debug("Set SecurityContextHolder to {}", authResult);

        this.rememberMeServices.orElse(this.defaultRememberMeServices).loginSuccess(request, response, authResult);
        this.eventPublisher.ifPresent(service -> service.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass())));
        this.successHandler.orElse(this.defaultSuccessHandler).onAuthenticationSuccess(request, response, authResult);
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
        this.securityContextHolderStrategy.orElse(this.defaultSecurityContextHolderStrategy).clearContext();
        log.trace("Failed to process authentication request", failed);
        log.trace("Cleared SecurityContextHolder");
        log.trace("Handling authentication failure");
        this.rememberMeServices.orElse(this.defaultRememberMeServices).loginFail(request, response);
        this.failureHandler.orElse(this.defaultFailureHandler).onAuthenticationFailure(request, response, failed);
    }
}
