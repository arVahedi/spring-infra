package examples.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.controller.ui.UIController;

import java.util.Map;

@Controller
@RequestMapping(value = {"/index", "/"})
public class IndexUIController implements UIController {

    public static final String VIEW_PAGE = "index.jsp";

    @Override
    public String render(Map<String, Object> model) {
        return VIEW_PAGE;
    }
}
