package examples.ui;

import annotation.IntegrationTest;
import examples.controller.ui.AccessDeniedUIController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@IntegrationTest
class AccessDeniedUIControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAccessDeniedPageRequested_thenReturnsViewWithRequestIp() throws Exception {
        this.mockMvc.perform(get("/403").with(request -> {
                    request.setRemoteAddr("192.168.1.10");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(model().attribute("request_ip", "192.168.1.10"))
                .andExpect(view().name(AccessDeniedUIController.VIEW_PAGE));
    }
}
