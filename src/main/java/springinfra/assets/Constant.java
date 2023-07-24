package springinfra.assets;

import org.springframework.http.HttpHeaders;

public class Constant {

    private Constant() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String PASSWORD_MASK = "******";
    public static final String AUTHORIZATION_TOKEN_COOKIE_NAME = HttpHeaders.AUTHORIZATION;
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORITY_POSTFIX = "_AUTHORITY";
}
