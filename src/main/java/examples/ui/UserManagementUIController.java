package examples.ui;

import examples.domain.User;
import examples.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springinfra.controller.ui.UIController;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/admin/user-management"})
@PreAuthorize("hasAuthority(T(springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
@RequiredArgsConstructor
public class UserManagementUIController implements UIController {

    public static final String VIEW_PAGE = "/userManagement.jsp";

    private final UserService userService;

    @Override
    public String render(Map<String, Object> model) {
        List<User> userList = this.userService.list();
        model.put("userList", userList);

        return VIEW_PAGE;
    }
}
