package org.springinfra.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;

import java.util.Collection;

/**
 * This class is a JwtDecoder which is responsible for verify/parse extracted String token into a Jwt object
 *
 */

@RequiredArgsConstructor
public class NimbusJwtDecoderDecorator implements JwtDecoder {

    private static final String DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s";

    private final NimbusJwtDecoder nimbusJwtDecoder;
    private final OAuth2TokenValidator<Jwt> extraJwtValidator;

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt jwt = this.nimbusJwtDecoder.decode(token);
        OAuth2TokenValidatorResult result = this.extraJwtValidator.validate(jwt);
        if (result.hasErrors()) {
            Collection<OAuth2Error> errors = result.getErrors();
            String validationErrorString = getJwtValidationExceptionMessage(errors);
            throw new JwtValidationException(validationErrorString, errors);
        }
        return jwt;
    }

    private String getJwtValidationExceptionMessage(Collection<OAuth2Error> errors) {
        for (OAuth2Error oAuth2Error : errors) {
            if (StringUtils.isNotBlank(oAuth2Error.getDescription())) {
                return String.format(DECODING_ERROR_MESSAGE_TEMPLATE, oAuth2Error.getDescription());
            }
        }
        return "Unable to validate Jwt";
    }
}
