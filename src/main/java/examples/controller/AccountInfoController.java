package examples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.assets.AuthorityType;
import springinfra.ws.controller.BaseController;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@Controller
@RequestMapping(value = {"/user/account"})
@RolesAllowed({AuthorityType.StringFormat.ACCOUNT_INFO_AUTHORITY})
public class AccountInfoController extends BaseController {

    @Override
    public String getViewPage() {
        return "/accountInfo.jsp";
    }
}
