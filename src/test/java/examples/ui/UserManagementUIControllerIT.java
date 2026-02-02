package examples.ui;

import annotation.IntegrationTest;
import annotation.WithMockJwt;
import examples.controller.ui.UserManagementUIController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springinfra.assets.AuthorityType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@IntegrationTest
class UserManagementUIControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAnonymousUserRequestsUserManagement_thenRedirectsToLogin() throws Exception {
        this.mockMvc.perform(get("/admin/user-management"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockJwt(username = "admin", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY})
    void whenAuthorizedUserRequestsUserManagement_thenReturnsView() throws Exception {
        this.mockMvc.perform(get("/admin/user-management"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userList"))
                .andExpect(view().name(UserManagementUIController.VIEW_PAGE));
    }
}
