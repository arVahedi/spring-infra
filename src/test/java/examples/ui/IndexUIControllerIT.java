package examples.ui;

import annotation.IntegrationTest;
import examples.controller.ui.IndexUIController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@IntegrationTest
class IndexUIControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenRootRequest_thenReturnsIndexView() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexUIController.VIEW_PAGE));
    }

    @Test
    void whenIndexRequest_thenReturnsIndexView() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexUIController.VIEW_PAGE));
    }
}
