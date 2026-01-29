package examples.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springinfra.controller.ui.DefaultUIController;

import java.util.Map;

@Controller
@RequestMapping(value = {"/index", "/"})
public class IndexUIController extends DefaultUIController {

    public static final String VIEW_PAGE = "index.jsp";

    @Override
    public String render(Map<String, Object> model) {
        return VIEW_PAGE;
    }
}
