package examples.rest;

import examples.dto.AuthRequest;
import examples.dto.AuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.lang.JoseException;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springinfra.assets.ClaimName;
import springinfra.assets.ResponseTemplate;
import springinfra.controller.rest.BaseRestController;
import springinfra.utility.identity.JwtUtility;

import java.util.HashMap;
import java.util.Map;

import static springinfra.assets.Constant.AUTHORIZATION_TOKEN_COOKIE_NAME;


@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/authenticate")
@Slf4j
@Tag(name = "Authentication API", description = "Authentication API")
@RequiredArgsConstructor
@Valid
public class AuthenticationController extends BaseRestController {

    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<ResponseTemplate<AuthResponse>> authenticate(@RequestBody @Validated AuthRequest authRequest) throws JoseException {
        log.info("Received authentication request with username [{}]", authRequest.getUsername());

        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        log.info("User [{}] has been authenticated successfully", authRequest.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimName.USERNAME, ((User) authentication.getPrincipal()).getUsername());
        String accessToken = JwtUtility.generateToken(claims);

        HttpCookie cookie = ResponseCookie.from(AUTHORIZATION_TOKEN_COOKIE_NAME, accessToken)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseTemplate<>(HttpStatus.OK,
                        AuthResponse.builder()
                                .token(accessToken)
                                .build()));
    }
}
