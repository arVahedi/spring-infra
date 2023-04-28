package examples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.ws.controller.BaseController;

import java.util.Map;

@Controller
@RequestMapping(value = {"/login"})
public class LoginController extends BaseController {

    @Override
    public String getViewPage() {
        return "login.jsp";
    }
}
