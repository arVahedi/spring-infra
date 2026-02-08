package org.springinfra.system.listener;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SuccessfulAuthenticationHandlerTest {

    @Test
    void whenGeneratingAuthorizationCookie_thenCookieIsHttpOnly() {
        var cookie = SuccessfulAuthenticationHandler.generateAuthorizationCookie(Optional.of("token"));

        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
    }
}
