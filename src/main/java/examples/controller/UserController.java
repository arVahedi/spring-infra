package examples.controller;

import examples.domain.User;
import examples.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.project.springinfra.ws.controller.BaseController;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/user"})
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @Override
    @GetMapping
    public String render(Map<String, Object> model) {
        List<User> userList = this.userService.findAll();
        model.put("userList", userList);

        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "userManagement.jsp";
//        return "redirect:/";
    }
}
