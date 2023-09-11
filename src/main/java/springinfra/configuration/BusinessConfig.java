package springinfra.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * This class is responsible for gathering business-related properties from configuration files and provides an encapsulated way
 * for them to be accessible all over of application.
 * <p>
 * In this way, we avoid getting access to properties being spread in different classes all over of encapsulation mechanism for accessing to them.
 * <p>
 * Bear in mind that this class should be the first bean (Highest precedence), since its properties can be used by all other beans when they are being created.
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class BusinessConfig implements BaseConfig {
}
