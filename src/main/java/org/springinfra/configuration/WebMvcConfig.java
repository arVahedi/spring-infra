package org.springinfra.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.RequestContextFilter;

@Slf4j
@Configuration
public class WebMvcConfig implements BaseConfig {

    /**
     * We register this bean here to reorder {@link RequestContextFilter} and put it at the first place of all filters.
     * So we can access to {@link ServletRequestAttributes} in all filters and classes without any concern
     *
     * @return {@link RequestContextFilter}
     */
    @Bean
    public RequestContextFilter requestContextFilter() {
        OrderedRequestContextFilter filter = new OrderedRequestContextFilter();
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }
}
