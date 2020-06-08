package personal.project.springinfra.ws.controller;

import java.util.Map;

public abstract class BaseController {

    public abstract String render(Map<String, Object> model);

    public abstract String getViewPage();
}
