package springinfra.configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
public class BeanConfig implements BaseConfig {

    public static final String INTERNAL_REST_TEMPLATE_BEAN_NAME = "internalRestTemplate";

    //TimedAspect makes @Timed usable on any arbitrary method.
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean(INTERNAL_REST_TEMPLATE_BEAN_NAME)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
