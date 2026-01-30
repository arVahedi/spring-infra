package examples.ui;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.MultiValueMap;
import org.springinfra.controller.ui.DefaultUIController;

import java.util.Map;

@Controller
@RequestMapping(value = {"/user/account"})
public class AccountInfoUIController extends DefaultUIController {

    public static final String VIEW_PAGE = "/accountInfo.jsp";

    @Override
    @PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).ACCOUNT_INFO_AUTHORITY)")
    public String render(Map<String, Object> model, @RequestParam MultiValueMap<String, String> requestParams) {
        return super.render(model, requestParams);
    }

    @Override
    public String render(Map<String, Object> model) {
        return VIEW_PAGE;
    }
}
