package examples.rest;

import annotation.IntegrationTest;
import annotation.WithMockJwt;
import examples.domain.Role;
import examples.dto.crud.RoleDto;
import examples.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springinfra.assets.AuthorityType;
import org.springinfra.controller.rest.BaseRestController;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@IntegrationTest
@WithMockJwt(username = "admin", authorities = {AuthorityType.StringFormat.ROLE_MANAGEMENT_AUTHORITY})
class RoleRestControllerIT {

    private static final String BASE_URL = BaseRestController.API_PATH_PREFIX_V1 + "/roles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void whenSaveRequestIsValid_thenReturnsOkResponse() throws Exception {
        RoleDto request = buildRoleRequest();

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.name").value("Administration"));
    }

    @Test
    void whenSaveRequestMissingName_thenReturnsBadRequest() throws Exception {
        RoleDto request = buildRoleRequest();
        request.setName("");

        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.result", hasItem("Role name is required")));
    }

    @Test
    void whenUpdateRequestIsValid_thenReturnsOkResponse() throws Exception {
        Role existingRole = persistRole("Viewer");
        RoleDto request = buildRoleRequest();
        request.setName("Editor");
        request.setVersion(1L);

        this.mockMvc.perform(put(BASE_URL + "/{id}", existingRole.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.name").value("Editor"));
    }

    @Test
    void whenUpdateRequestMissingVersion_thenReturnsBadRequest() throws Exception {
        Role existingRole = persistRole("Viewer");
        RoleDto request = buildRoleRequest();

        this.mockMvc.perform(put(BASE_URL + "/{id}", existingRole.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.result", hasItem("version is required")));
    }

    @Test
    void whenDeleteRequest_thenReturnsNoContent() throws Exception {
        Role existingRole = persistRole("ToDelete");

        this.mockMvc.perform(delete(BASE_URL + "/{id}", existingRole.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenFindRequest_thenReturnsOkResponse() throws Exception {
        Role savedRole = persistRole("Finder");

        this.mockMvc.perform(get(BASE_URL + "/{id}", savedRole.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.name").value("Finder"));
    }

    @Test
    void whenListRequest_thenReturnsOkResponse() throws Exception {
        persistRole("ListRoleOne");
        persistRole("ListRoleTwo");

        this.mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[*].name", hasItem("ListRoleOne")))
                .andExpect(jsonPath("$.result[*].name", hasItem("ListRoleTwo")));
    }

    private RoleDto buildRoleRequest() {
        RoleDto roleDto = new RoleDto();
        roleDto.setName("Administration");
        roleDto.setAuthorities(List.of(AuthorityType.USER_MANAGEMENT_AUTHORITY, AuthorityType.ROLE_MANAGEMENT_AUTHORITY));
        return roleDto;
    }

    private Role persistRole(String name) {
        Role role = new Role();
        role.setName(name);
        role.setAuthorities(List.of(AuthorityType.ACCOUNT_INFO_AUTHORITY));
        return this.roleRepository.save(role);
    }
}
