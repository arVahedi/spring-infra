package examples.rest;

import annotation.IntegrationTest;
import annotation.WithMockJwt;
import examples.assets.UserStatus;
import examples.model.dto.request.CreateCredentialRequest;
import examples.model.dto.request.CreateUserRequest;
import examples.model.entity.Credential;
import examples.model.entity.Role;
import examples.model.entity.User;
import examples.repository.CredentialRepository;
import examples.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springinfra.assets.AuthorityType;
import org.springinfra.assets.Constant;
import org.springinfra.controller.rest.BaseRestController;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@IntegrationTest
@WithMockJwt(username = "admin", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY, AuthorityType.StringFormat.ROLE_MANAGEMENT_AUTHORITY})
class CredentialRestControllerIT {

    private static final String BASE_URL = BaseRestController.API_PATH_PREFIX_V1 + "/credentials";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void whenSaveRequestIsValid_thenReturnsOkResponse() throws Exception {
        CreateCredentialRequest request = new CreateCredentialRequest("admin", "secret123",
                buildUserRequest(), Collections.emptyList());

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.username").value("admin"))
                .andExpect(jsonPath("$.result.id").isNotEmpty());
    }

    @Test
    void whenSaveRequestUsesPasswordMask_thenReturnsBadRequest() throws Exception {
        CreateCredentialRequest request = new CreateCredentialRequest("username", Constant.PASSWORD_MASK,
                buildUserRequest(), Collections.emptyList());

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.result", hasItem("Password value is not valid")));
    }

    @Test
    void whenPatchRequestIsValid_thenReturnsOkResponse() throws Exception {
        Credential existingCredential = persistCredential("before");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingCredential.getPublicId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"patched\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
    }

    @Test
    void whenPatchRequestContainsRoleIds_thenReturnsOkResponse() throws Exception {
        Credential existingCredential = persistCredential("role-user");
        Role role = persistRole("ROLE_USER");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingCredential.getPublicId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roles\":[\"" + role.getPublicId() + "\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()));
    }

    @Test
    void whenPatchRequestHasUnknownField_thenReturnsBadRequest() throws Exception {
        Credential existingCredential = persistCredential("before");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingCredential.getPublicId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notAField\":\"value\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail", containsString("[notAField]: Property does not exist")));
    }

    @Test
    void whenDeleteRequest_thenReturnsNoContent() throws Exception {
        Credential existingCredential = persistCredential("to-delete");

        this.mockMvc.perform(delete(BASE_URL + "/{id}", existingCredential.getPublicId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenFindRequest_thenReturnsOkResponse() throws Exception {
        Credential savedCredential = persistCredential("finder");

        this.mockMvc.perform(get(BASE_URL + "/{id}", savedCredential.getPublicId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.username").value("finder"));
    }

    @Test
    void whenListRequest_thenReturnsOkResponse() throws Exception {
        persistCredential("list-user-one");
        persistCredential("list-user-two");

        this.mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[*].username", hasItem("list-user-one")))
                .andExpect(jsonPath("$.result[*].username", hasItem("list-user-two")));
    }

    @Test
    @WithMockJwt(username = "alice", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY, AuthorityType.StringFormat.ACCOUNT_INFO_AUTHORITY})
    void whenAccountInfoRequest_thenReturnsOkResponse() throws Exception {
        persistCredential("alice");

        this.mockMvc.perform(get(BASE_URL + "/account-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.username").value("alice"));
    }

    @Test
    @WithMockJwt(username = "alice")
    void whenAccountInfoRequestAndUserDoesNotHavePermission_thenReturnsForbidden() throws Exception {
        persistCredential("alice");

        this.mockMvc.perform(get(BASE_URL + "/account-info"))
                .andExpect(status().isForbidden());
    }

    private CreateUserRequest buildUserRequest() {
        return new CreateUserRequest("John", "Doe", "john.doe@example.com", "123-456-7890", UserStatus.ACTIVE);
    }

    private Credential persistCredential(String username) {
        Credential credential = new Credential();
        credential.setUsername(username);
        credential.setPassword("hashed");
        credential.setUser(buildUserEntity(username));
        return this.credentialRepository.save(credential);
    }

    private Role persistRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        role.setAuthorities(List.of(AuthorityType.USER_MANAGEMENT_AUTHORITY));
        return this.roleRepository.save(role);
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
