package springinfra.controller.rest;

import examples.dto.AuthRequest;
import examples.dto.AuthResponse;
import springinfra.service.UsernamePasswordAuthenticationService;
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
import springinfra.configuration.security.idp.BuildInIdentityProviderConfig;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

import static springinfra.system.listener.SuccessfulAuthenticationHandler.AUTH_TOKEN_REQUEST_ATTRIBUTE;


@RestController
@ConditionalOnBean(BuildInIdentityProviderConfig.class)
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/authenticate")
@Slf4j
@Tag(name = "Authentication API", description = "Authentication API")
@RequiredArgsConstructor
@Valid
public class AuthenticationController extends BaseRestController {

    public final UsernamePasswordAuthenticationService authenticationService;

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
