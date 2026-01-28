package springinfra.database;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;
import springinfra.database.listener.EnhancedDeleteEventListener;

/**
 * This class is responsible for managing event listeners of Hibernate.
 * So far, it registers our custom {@link EnhancedDeleteEventListener} which handles both logical and physical deletions automatically.
 */

@Component
@RequiredArgsConstructor
public class HibernateEventListenerManager {

    private final EntityManagerFactory emf;
    private final EnhancedDeleteEventListener enhancedDeleteEventListener;

    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = this.emf.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.DELETE).clearListeners();
        registry.getEventListenerGroup(EventType.DELETE).prependListener(this.enhancedDeleteEventListener);
    }
}
