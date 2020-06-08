package personal.project.springinfra.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = {"/index", "/"})
public class IndexController extends BaseController {

    @Override
    @GetMapping
    public String render(Map<String, Object> model) {
        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "index.jsp";
    }
}
