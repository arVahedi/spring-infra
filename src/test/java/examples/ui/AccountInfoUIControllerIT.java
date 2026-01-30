package examples.ui;

import annotation.IntegrationTest;
import annotation.WithMockJwt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springinfra.assets.AuthorityType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@IntegrationTest
class AccountInfoUIControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockJwt(username = "user", authorities = {AuthorityType.StringFormat.ACCOUNT_INFO_AUTHORITY})
    void whenAuthorizedUserRequestsAccountInfo_thenReturnsView() throws Exception {
        this.mockMvc.perform(get("/user/account"))
                .andExpect(status().isOk())
                .andExpect(view().name(AccountInfoUIController.VIEW_PAGE));
    }

    @Test
    @WithMockJwt(username = "user", authorities = {AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY})
    void whenAuthenticatedUserWithoutAuthorityRequestsAccountInfo_thenRedirectsTo403() throws Exception {
        this.mockMvc.perform(get("/user/account"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }
}
