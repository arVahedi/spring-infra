package examples.controller;

import examples.domain.User;
import examples.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.assets.AuthorityType;
import springinfra.ws.controller.BaseController;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/admin/user-management"})
@RolesAllowed({AuthorityType.StringFormat.USER_MANAGEMENT_AUTHORITY})
@RequiredArgsConstructor
public class UserManagementController extends BaseController {

    private final UserService userService;

    @Override
    public String render(Map<String, Object> model) {
        List<User> userList = this.userService.list();
        model.put("userList", userList);

        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "/userManagement.jsp";
    }
}
