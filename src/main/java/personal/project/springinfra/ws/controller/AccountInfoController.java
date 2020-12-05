package personal.project.springinfra.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.project.springinfra.assets.AuthorityType;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@Controller
@RequestMapping(value = {"/user/account-info"})
@RolesAllowed({AuthorityType.String.ACCOUNT_INFO_AUTHORITY})
public class AccountInfoController extends BaseController {

    @Override
    public String render(Map<String, Object> model) {
        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "/accountInfo.jsp";
    }
}
