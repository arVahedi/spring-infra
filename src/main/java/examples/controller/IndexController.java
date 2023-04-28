package examples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.ws.controller.BaseController;

@Controller
@RequestMapping(value = {"/index", "/"})
public class IndexController extends BaseController {

    @Override
    public String getViewPage() {
        return "index.jsp";
    }
}
