package springinfra.controller.ui;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * The parent of all UI controllers (in MVC model) before going to presentation pages
 */

public interface UIController {

    @GetMapping
    String render(Map<String, Object> model);
}
