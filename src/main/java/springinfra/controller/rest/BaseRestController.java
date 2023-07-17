package springinfra.controller.rest;

import org.springframework.validation.annotation.Validated;

@Validated
public abstract class BaseRestController {

    public static final String API_PATH_PREFIX_V1 = "/api/v1";

}
