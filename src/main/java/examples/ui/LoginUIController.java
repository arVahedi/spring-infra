package examples.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.controller.ui.UIController;
import springinfra.utility.identity.IdentityUtility;

import java.util.Map;

@Controller
@RequestMapping(value = {"/login"})
public class LoginUIController implements UIController {

    public static final String VIEW_PAGE = "/login.jsp";

    @Override
    public String render(Map<String, Object> model) {
        if (IdentityUtility.isAuthenticated()) {
            return "redirect:/";
        }
        return VIEW_PAGE;
    }
}
