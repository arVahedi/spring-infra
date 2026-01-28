package examples.ui;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.controller.ui.DefaultUIController;

import java.util.Map;

@Controller
@RequestMapping(value = {"/user/account"})
@PreAuthorize("hasAuthority(T(springinfra.assets.AuthorityType).ACCOUNT_INFO_AUTHORITY)")
public class AccountInfoUIController extends DefaultUIController {

    public static final String VIEW_PAGE = "/accountInfo.jsp";

    @Override
    public String render(Map<String, Object> model) {
        return VIEW_PAGE;
    }
}
