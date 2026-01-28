package springinfra.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * This filter is responsible for handling all exceptions (regardless those are thrown by a controller, other filters or somewhere else)
 * just before returning them to the client.
 * <p>
 * Since the RestExceptionAdvisor can only handle the exceptions that are thrown by a controller, we need this filter here to handle
 * exceptions that are mostly thrown by another filter (Because filters are executed before the controllers). Most of those exceptions are
 * Spring build-in exceptions which are thrown by Spring filters, but basically this filter works for all exceptions fine.
 * <p>
 * <strong>Bear in mind that the order of this filter is one less than {@link Ordered#HIGHEST_PRECEDENCE}, because it better be the first filter to make sure
 * it's able to catch all exceptions, on the other hand, the highest priority belongs to {@link org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter}.
 * So the only highest value available for the current filter is {@link Ordered#HIGHEST_PRECEDENCE} minus one</strong>
 */

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.info("Trying to handle caught exception {} in {}", ex.getClass().getSimpleName(), this.getClass().getSimpleName());
            ModelAndView modelAndView = this.handlerExceptionResolver.resolveException(request, response, null, ex);
            if (modelAndView == null) {
                log.warn("Couldn't fine proper handler for exception {}", ex.getClass().getName());
                throw ex;
            }
        }
    }


}
