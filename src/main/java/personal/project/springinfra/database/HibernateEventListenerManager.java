package personal.project.springinfra.database;

import lombok.RequiredArgsConstructor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import personal.project.springinfra.database.listener.EnhancedDeleteEventListener;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
@RequiredArgsConstructor
public class HibernateEventListenerManager {

    private final EntityManagerFactory emf;
    private final EnhancedDeleteEventListener enhancedDeleteEventListener;

    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = this.emf.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.DELETE).clear();
        registry.getEventListenerGroup(EventType.DELETE).prependListener(this.enhancedDeleteEventListener);
    }
}
