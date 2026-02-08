package examples.controller.ui;

import examples.model.dto.view.UserView;
import examples.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springinfra.controller.ui.DefaultUIController;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/admin/user-management"})
@RequiredArgsConstructor
public class UserManagementUIController extends DefaultUIController {

    public static final String VIEW_PAGE = "/userManagement.jsp";

    private final UserService userService;

    @Override
    @PreAuthorize("hasAuthority(T(org.springinfra.assets.AuthorityType).USER_MANAGEMENT_AUTHORITY)")
    public String render(Map<String, Object> model, @RequestParam MultiValueMap<String, String> requestParams) {
        return super.render(model, requestParams);
    }

    @Override
    public String render(Map<String, Object> model) {
        List<UserView> userList = this.userService.list(Pageable.unpaged());
        model.put("userList", userList);

        return VIEW_PAGE;
    }
}
