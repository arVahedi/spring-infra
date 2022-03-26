package springinfra.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.assets.AuthorityType;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@Controller
@RequestMapping(value = {"/admin/user-management"})
@RolesAllowed({AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY})
public class UserManagementController extends BaseController {

    @Override
    public String render(Map<String, Object> model) {
        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "/userManagement.jsp";
    }
}