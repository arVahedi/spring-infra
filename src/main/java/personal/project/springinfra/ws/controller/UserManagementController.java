package personal.project.springinfra.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.project.springinfra.assets.AuthorityType;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

@Controller
@RequestMapping(value = {"/admin/user-management"})
@RolesAllowed({AuthorityType.String.USER_MANAGEMENT_AUTHORITY})
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
