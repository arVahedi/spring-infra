package springinfra.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import springinfra.SpringContext;
import springinfra.configuration.OpenApiConfig;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Gladiator on 7/22/2017 AD.
 */
@Slf4j
@WebFilter(filterName = "DirectlyJSPAccessFilter", urlPatterns = {"*.jsp", "*.html"})
public class DirectlyJSPAccessFilter implements BaseServletFilter {

    private Optional<OpenApiConfig> openApiConfig = Optional.empty();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (this.openApiConfig.isEmpty()) {
            this.openApiConfig = Optional.ofNullable(SpringContext.getApplicationContext().getBean(OpenApiConfig.class));
        }

        String requestUri = ((HttpServletRequest) servletRequest).getRequestURI();
        //Allow access to swagger UI
        if ((this.openApiConfig.isPresent() && requestUri.equalsIgnoreCase(this.openApiConfig.get().getSwaggerUiPath())) ||
                requestUri.endsWith(OpenApiConfig.SWAGGER_HTML_URI)) {
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
