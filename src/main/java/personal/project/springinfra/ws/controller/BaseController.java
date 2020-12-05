package personal.project.springinfra.ws.controller;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public abstract class BaseController {

    @GetMapping
    public String render(Map<String, Object> model) {
        return getViewPage();
    }

    public abstract String getViewPage();
}
