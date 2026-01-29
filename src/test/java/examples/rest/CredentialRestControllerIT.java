package examples.rest;

import annotation.IntegrationTest;
import examples.assets.UserStatus;
import examples.domain.Credential;
import examples.domain.User;
import examples.dto.crud.CredentialDto;
import examples.dto.crud.UserDto;
import examples.repository.CredentialRepository;
import lombok.extern.slf4j.Slf4j;
import annotation.WithMockJwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.SpringInfraApplication;
import org.springinfra.assets.AuthorityType;
import org.springinfra.controller.rest.BaseRestController;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@IntegrationTest
@SpringBootTest(classes = SpringInfraApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockJwt(username = "admin", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY, AuthorityType.StringFormat.ROLE_MANAGEMENT_AUTHORITY})
class CredentialRestControllerIT {

    private static final String BASE_URL = BaseRestController.API_PATH_PREFIX_V1 + "/credentials";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CredentialRepository credentialRepository;

    @Test
    void whenSaveRequestIsValid_thenReturnsOkResponse() throws Exception {
        CredentialDto request = buildCredentialRequest();

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.result.username").value("admin"));
    }

    @Test
    void whenSaveRequestUsesPasswordMask_thenReturnsBadRequest() throws Exception {
        CredentialDto request = buildCredentialRequest();
        request.setPassword("******");

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.result", hasItem("Password value is not valid")));
    }

    @Test
    void whenUpdateRequestIsValid_thenReturnsOkResponse() throws Exception {
        Credential existingCredential = persistCredential("initial");
        CredentialDto request = buildCredentialRequest();
        request.setUsername("updated");
        request.setVersion(1L);

        this.mockMvc.perform(put(BASE_URL + "/{id}", existingCredential.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.result.username").value("updated"));
    }

    @Test
    void whenUpdateRequestMissingVersion_thenReturnsBadRequest() throws Exception {
        Credential existingCredential = persistCredential("initial");
        CredentialDto request = buildCredentialRequest();

        this.mockMvc.perform(put(BASE_URL + "/{id}", existingCredential.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.result", hasItem("version is required")));
    }

    @Test
    void whenPatchRequestIsValid_thenReturnsOkResponse() throws Exception {
        Credential existingCredential = persistCredential("before");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingCredential.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"patched\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.result.username").value("patched"));
    }

    @Test
    void whenPatchRequestHasUnknownField_thenReturnsBadRequest() throws Exception {
        Credential existingCredential = persistCredential("before");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingCredential.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notAField\":\"value\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.result", hasItem("[notAField]: Property does not exist")));
    }

    @Test
    void whenDeleteRequest_thenReturnsNoContent() throws Exception {
        Credential existingCredential = persistCredential("todelete");

        this.mockMvc.perform(delete(BASE_URL + "/{id}", existingCredential.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenFindRequest_thenReturnsOkResponse() throws Exception {
        Credential savedCredential = persistCredential("finder");

        this.mockMvc.perform(get(BASE_URL + "/{id}", savedCredential.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.result.username").value("finder"));
    }

    @Test
    void whenListRequest_thenReturnsOkResponse() throws Exception {
        persistCredential("listuser-one");
        persistCredential("listuser-two");

        this.mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[*].username", hasItem("listuser-one")))
                .andExpect(jsonPath("$.result[*].username", hasItem("listuser-two")));
    }

    @Test
    @WithMockJwt(username = "alice", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY, AuthorityType.StringFormat.ACCOUNT_INFO_AUTHORITY})
    void whenAccountInfoRequest_thenReturnsOkResponse() throws Exception {
        persistCredential("alice");

        this.mockMvc.perform(get(BASE_URL + "/account-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.result.username").value("alice"));
    }

    private CredentialDto buildCredentialRequest() {
        CredentialDto dto = new CredentialDto();
        dto.setUsername("admin");
        dto.setPassword("secret123");
        dto.setUser(buildUserRequest());
        return dto;
    }

    private UserDto buildUserRequest() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPhone("123-456-7890");
        userDto.setStatus(UserStatus.ACTIVE);
        return userDto;
    }

    private Credential persistCredential(String username) {
        Credential credential = new Credential();
        credential.setUsername(username);
        credential.setPassword("hashed");
        credential.setUser(buildUserEntity(username));
        return this.credentialRepository.saveAndFlush(credential);
    }

    private User buildUserEntity(String usernameSeed) {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(usernameSeed + "@example.com");
        user.setPhone("123-456-7890");
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
}
