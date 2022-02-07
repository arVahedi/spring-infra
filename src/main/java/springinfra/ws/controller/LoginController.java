package springinfra.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = {"/login"})
public class LoginController extends BaseController {

    @Override
    public String render(Map<String, Object> model) {
        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "login.jsp";
    }
}
