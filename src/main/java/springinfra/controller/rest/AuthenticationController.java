package springinfra.controller.rest;

import examples.dto.AuthRequest;
import examples.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.assets.ResponseTemplate;
import springinfra.configuration.security.idp.BuiltInIdentityProviderConfig;
import springinfra.service.UsernamePasswordAuthenticationService;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

import static springinfra.system.listener.SuccessfulAuthenticationHandler.AUTH_TOKEN_REQUEST_ATTRIBUTE;

/**
 * This is an endpoint that we support for authenticating users by their username and password in a built-in authentication mechanism.
 * <p>
 * * Consider that the word built-in means we don't use any external authorization server like what we use in the OIDC protocol. That's the reason
 * * why creation of this bean depends on existing an instance of {@link BuiltInIdentityProviderConfig}, because just in that case we are supporting
 * * a built-in authentication mechanism and need to this service, otherwise we are likely using OIDC protocol and an external authorization server.
 */

@RestController
@ConditionalOnBean(BuiltInIdentityProviderConfig.class)
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/authenticate")
@Slf4j
@Tag(name = "Authentication API", description = "Authentication API")
@RequiredArgsConstructor
@Valid
public class AuthenticationController extends BaseRestController {

    public final UsernamePasswordAuthenticationService authenticationService;

    @Operation(summary = "Authenticating user", description = "Authenticating an existing user via username and password")
    @PostMapping
    public ResponseEntity<ResponseTemplate<AuthResponse>> authenticate(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated AuthRequest authRequest) throws ServletException, IOException {
        try {
            log.info("Received authentication request with username [{}]", authRequest.getUsername());
            this.authenticationService.authenticate(request, response, authRequest);
            return ResponseEntity.ok()
                    .body(new ResponseTemplate<>(HttpStatus.OK,
                            AuthResponse.builder()
                                    .token((String) request.getAttribute(AUTH_TOKEN_REQUEST_ATTRIBUTE))
                                    .build()));
        } catch (OperationNotSupportedException ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.GONE).body(new ResponseTemplate<>(HttpStatus.GONE));
        }
    }
}
