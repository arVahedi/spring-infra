package examples.ui;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springinfra.controller.ui.UIController;

import java.util.Map;

@Controller
@RequestMapping(value = {"/403"})
public class AccessDeniedUIController implements UIController {

    public static final String VIEW_PAGE = "/403.jsp";

    @Override
    public String render(Map<String, Object> model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        model.put("request_ip", request.getRemoteAddr());
        return VIEW_PAGE;
    }
}
