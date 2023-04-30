package springinfra.ws.endpoint;

import org.springframework.validation.annotation.Validated;

@Validated
public abstract class BaseApi {

    public static final String API_PATH_PREFIX_V1 = "/api/v1";
}
