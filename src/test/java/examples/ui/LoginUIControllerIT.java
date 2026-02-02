package examples.ui;

import annotation.IntegrationTest;
import annotation.WithMockJwt;
import examples.controller.ui.LoginUIController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@IntegrationTest
class LoginUIControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAnonymousUserRequestsLogin_thenReturnsBuiltInLoginView() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name(LoginUIController.BUILT_IN_LOGIN_JSP));
    }

    @Test
    @WithMockJwt(username = "admin")
    void whenAuthenticatedUserRequestsLogin_thenRedirectsToIndex() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}
