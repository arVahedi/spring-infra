package springinfra.utility.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import springinfra.controller.rest.BaseRestController;

@Slf4j
@UtilityClass
public class HttpRequestUtility {

    public boolean isUiRequest(HttpServletRequest request) {
        return !request.getRequestURI().toLowerCase().startsWith(BaseRestController.API_PATH_PREFIX.toLowerCase())
                && request.getMethod().equalsIgnoreCase(HttpMethod.GET.name());
    }
}
