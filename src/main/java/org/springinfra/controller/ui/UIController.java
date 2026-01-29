package org.springinfra.controller.ui;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * The parent of all UI controllers (in MVC model) before going to presentation pages
 */

public interface UIController {

    @GetMapping
    String render(Map<String, Object> model, @RequestParam MultiValueMap<String, String> requestParams);
}
