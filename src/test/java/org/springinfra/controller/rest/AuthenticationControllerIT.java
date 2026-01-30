package org.springinfra.controller.rest;

import annotation.IntegrationTest;
import examples.dto.AuthRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@IntegrationTest
class AuthenticationControllerIT {

    private static final String BASE_URL = BaseRestController.API_PATH_PREFIX_V1 + "/authenticate";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        String encodedPassword = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode(PASSWORD);
        registry.add("security.idp.module", () -> "inMemoryIdentityProviderModule");
        registry.add("security.idp.in-memory.credentials[0]",
                () -> USERNAME + ";" + encodedPassword + ";USER_MANAGEMENT_AUTHORITY;ACCOUNT_INFO_AUTHORITY;ROLE_MANAGEMENT_AUTHORITY");
    }

    @Test
    void whenAuthenticateRequestIsValid_thenReturnsOkResponse() throws Exception {
        AuthRequest request = buildAuthRequest(USERNAME, PASSWORD);

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.token").value(not(emptyOrNullString())));
    }

    @Test
    void whenAuthenticateRequestHasInvalidPassword_thenReturnsUnauthorized() throws Exception {
        AuthRequest request = buildAuthRequest(USERNAME, "wrong-password");

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    private AuthRequest buildAuthRequest(String username, String password) {
        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }
}
