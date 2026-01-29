package org.springinfra.controller.rest;

import org.springframework.validation.annotation.Validated;

/**
 * The parent of all API controllers
 */

@Validated
public abstract class BaseRestController {

    public static final String API_PATH_PREFIX = "/api";
    public static final String API_PATH_PREFIX_V1 = API_PATH_PREFIX + "/v1";
}
