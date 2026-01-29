package org.springinfra.configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * This configuration class is responsible for managing independent beans
 * <p>
 * <strong>Independent</strong> means these beans are not related to other specific configuration classes, otherwise you can find them in
 * their own configuration classes.
 */

@Configuration
@RequiredArgsConstructor
public class BeanConfig implements BaseConfig {

    public static final String INTERNAL_REST_TEMPLATE_BEAN_NAME = "internalRestTemplate";

    //TimedAspect makes @Timed usable on any arbitrary method.
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean(INTERNAL_REST_TEMPLATE_BEAN_NAME)
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}

