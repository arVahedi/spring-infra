package examples.rest;

import annotation.IntegrationTest;
import annotation.WithMockJwt;
import examples.assets.UserStatus;
import examples.domain.User;
import examples.dto.crud.UserDto;
import examples.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
@WithMockJwt(username = "admin", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY})
class UserRestControllerIT {

    private static final String BASE_URL = BaseRestController.API_PATH_PREFIX_V1 + "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenSaveRequestIsValid_thenReturnsOkResponse() throws Exception {
        UserDto request = buildUserRequest();

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.firstName").value("John"));
    }

    @Test
    void whenSaveRequestMissingFirstName_thenReturnsBadRequest() throws Exception {
        UserDto request = buildUserRequest();
        request.setFirstName(null);

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.result", hasItem("First-Name is required")));
    }

    @Test
    void whenUpdateRequestIsValid_thenReturnsOkResponse() throws Exception {
        User existingUser = persistUser("Before");
        UserDto request = buildUserRequest();
        request.setFirstName("Updated");
        request.setVersion(1L);

        this.mockMvc.perform(put(BASE_URL + "/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.firstName").value("Updated"));
    }

    @Test
    void whenUpdateRequestMissingVersion_thenReturnsBadRequest() throws Exception {
        User existingUser = persistUser("Before");
        UserDto request = buildUserRequest();

        this.mockMvc.perform(put(BASE_URL + "/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.result", hasItem("version is required")));
    }

    @Test
    void whenPatchRequestIsValid_thenReturnsOkResponse() throws Exception {
        User existingUser = persistUser("Before");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Patched\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.firstName").value("Patched"));
    }

    @Test
    void whenPatchRequestHasUnknownField_thenReturnsBadRequest() throws Exception {
        User existingUser = persistUser("Before");

        this.mockMvc.perform(patch(BASE_URL + "/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"notAField\":\"value\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.result", hasItem("[notAField]: Property does not exist")));
    }

    @Test
    void whenDeleteRequest_thenReturnsNoContent() throws Exception {
        User existingUser = persistUser("ToDelete");

        this.mockMvc.perform(delete(BASE_URL + "/{id}", existingUser.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenFindRequest_thenReturnsOkResponse() throws Exception {
        User savedUser = persistUser("Finder");

        this.mockMvc.perform(get(BASE_URL + "/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.firstName").value("Finder"));
    }

    @Test
    void whenListRequest_thenReturnsOkResponse() throws Exception {
        persistUser("ListUserOne", "list.one@example.com");
        persistUser("ListUserTwo", "list.two@example.com");

        this.mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[*].email", hasItem("list.one@example.com")))
                .andExpect(jsonPath("$.result[*].email", hasItem("list.two@example.com")));
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

    private User persistUser(String firstName) {
        return persistUser(firstName, firstName.toLowerCase() + "@example.com");
    }

    private User persistUser(String firstName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName("Doe");
        user.setEmail(email);
        user.setPhone("123-456-7890");
        user.setStatus(UserStatus.ACTIVE);
        return this.userRepository.save(user);
    }
}
