package springinfra.controller.ui;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public interface UIController {

    @GetMapping
    String render(Map<String, Object> model);
}
