package org.springinfra.utility.identity;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;
import org.springinfra.assets.ClaimName;
import org.springinfra.utility.cryptographic.JksUtility;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//  https://github.com/felx/jose4j-wiki/blob/master/JWT%20Examples.md
@Slf4j
@UtilityClass
public class JwtUtility {

    public final String ISSUER = "SPRING-INFRA";
    public final String AUDIENCE = "GENERAL_PUBLIC";
    public final String SUBJECT = "USER_IDENTITY";
    public final int EXPIRATION_TIME_MINUTES = 24 * 60;
    public final int NOT_VALID_IN_MINUTES_BEFORE = 1;

    private RsaJsonWebKey rsaJsonWebKey;
    private JwtConsumer jwtConsumer;

    static {
        try {
            /*rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");*/

            KeyStore keyStore = JksUtility.getKeyStore(JksUtility.KeyStoreType.JKS, "spring-infra.jks", "123456");
            KeyPair keyPair = JksUtility.getKeyPair(keyStore, "Spring-infra", "123456");
            rsaJsonWebKey = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(keyPair.getPublic());
            rsaJsonWebKey.setPrivateKey(keyPair.getPrivate());

            // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will be used to validate and process
            // the JWT. If the JWT is encrypted too, you need only provide a decryption key or decryption key resolver to
            // the builder.
            jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setExpectedIssuer(ISSUER) // whom the JWT needs to have been issued by
                    .setExpectedAudience(AUDIENCE) // to whom the JWT is intended for
                    .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                    .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                            AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                    .build(); // create the JwtConsumer instance
        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | JoseException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(Map<String, Object> extraClaimsMap) throws JoseException {
        var claims = new JwtClaims();

        claims.setIssuer(ISSUER);  // who creates the token and signs it
        claims.setAudience(AUDIENCE); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(EXPIRATION_TIME_MINUTES); // time when the token will expire (10 minutes from now)
        claims.setNotBeforeMinutesInThePast(NOT_VALID_IN_MINUTES_BEFORE); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject(SUBJECT); // the subject/principal is whom the token is about
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)

        // additional claims/attributes about the subject can be added
        extraClaimsMap.forEach(claims::setClaim);

        var jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }

    public Map<String, Object> getClaims(String token) throws InvalidJwtException {
        return jwtConsumer.processToClaims(token).getClaimsMap();
    }

    public Optional<String> getUsernameFromToken(String token) throws InvalidJwtException {
        return getClaim(token, ClaimName.USERNAME);
    }

    public Optional<String> getClaim(String token, String claimName) throws InvalidJwtException {
        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
        return Optional.ofNullable(jwtClaims.getClaimValueAsString(claimName));
    }

    public Map<String, Object> getHeaders(String token) throws InvalidJwtException, JoseException {
        JwtContext jwtContext = jwtConsumer.process(token);
        List<JsonWebStructure> jsonWebStructures = jwtContext.getJoseObjects();
        if (!jsonWebStructures.isEmpty()) {
            return JsonUtil.parseJson(jsonWebStructures.get(0).getHeaders().getFullHeaderAsJsonString());
        }
        return Collections.emptyMap();
    }

    public Map<String, Object> getHeadersWithoutValidation(String token) throws InvalidJwtException, JoseException {
        JwtConsumer jwtNoValidationConsumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();

        JwtContext jwtContext = jwtNoValidationConsumer.process(token);
        List<JsonWebStructure> jsonWebStructures = jwtContext.getJoseObjects();
        if (!jsonWebStructures.isEmpty()) {
            return JsonUtil.parseJson(jsonWebStructures.get(0).getHeaders().getFullHeaderAsJsonString());
        }
        return Collections.emptyMap();
    }

    public void validateToken(String token) throws InvalidJwtException {
        jwtConsumer.process(token);
    }
}
