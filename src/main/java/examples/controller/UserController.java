package examples.controller;

import examples.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import examples.domain.User;
import personal.project.springinfra.ws.controller.BaseController;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/user"})
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping
    public String render(Map<String, Object> model) {
        List<User> userList = this.userService.findAll();
        model.put("userList", userList);

        return getViewPage();
    }

    @Override
    public String getViewPage() {
        return "user.jsp";
//        return "redirect:/";
    }
}
