package personal.project.springinfra.ws.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Gladiator on 7/22/2017 AD.
 */
@Slf4j
@WebFilter(filterName = "DirectlyJSPAccessFilter", urlPatterns = {"*.jsp", "*.html"})
public class DirectlyJSPAccessFilter extends BaseFilter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //Allow access to swagger UI
        if (((HttpServletRequest) servletRequest).getRequestURI().equalsIgnoreCase("/doc/api/swagger-ui/index.html")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(404);
        }
    }

    @Override
    public void destroy() {

    }
}
