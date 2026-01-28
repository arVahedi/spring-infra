package springinfra.security.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import springinfra.SpringContext;
import springinfra.configuration.BeanConfig;

import java.net.URI;

/**
 * This class is written to handle single sing-out ability. We try to validate the OIDC session in our cloud authorization server
 * to ensure the corresponding session of the ID token is still valid there. Otherwise, if the ID token is not active,
 * means a sign-out has already occurred for that token, and it MUST NOT be valid anymore.
 */

@Slf4j
public class OidcCloudSessionValidator implements OAuth2TokenValidator<Jwt> {

    private final URI introspectionEndpoint;
    private final String clientId;
    private final String clientSecret;

    public OidcCloudSessionValidator(URI introspectionEndpoint, String clientId, String clientSecret) {
        this.introspectionEndpoint = introspectionEndpoint;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
            payload.add("token", token.getTokenValue());
            payload.add("client_id", this.clientId);
            payload.add("client_secret", this.clientSecret);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<IntrospectionResponse> introspectionResponse = SpringContext.getApplicationContext().getBean(BeanConfig.INTERNAL_REST_TEMPLATE_BEAN_NAME, RestTemplate.class).postForEntity(this.introspectionEndpoint, entity, IntrospectionResponse.class);
            if (introspectionResponse.getBody() == null || !introspectionResponse.getBody().isActive()) {
                OAuth2Error error = new OAuth2Error(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "The cloud session is not active", this.introspectionEndpoint.toString());
                return OAuth2TokenValidatorResult.failure(error);
            }

            return OAuth2TokenValidatorResult.success();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            OAuth2Error error = new OAuth2Error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), this.introspectionEndpoint.toString());
            return OAuth2TokenValidatorResult.failure(error);
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IntrospectionResponse {
        private boolean active;
    }
}
