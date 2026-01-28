package springinfra.database.listener;

import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.spi.DeleteContext;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;
import springinfra.model.domain.LogicalDeletableDomain;

/**
 * This is an enhanced delete event listener for Hibernate that can handle both logical and physical deletions automatically
 * according to type of entity object.
 *
 * @see LogicalDeletableDomain
 */

@Component
public class EnhancedDeleteEventListener extends DefaultDeleteEventListener {

    @Override
    public void onDelete(DeleteEvent event, DeleteContext transientEntities) {
        Object eventObject = event.getObject();
        if (eventObject instanceof LogicalDeletableDomain<?> logicalDeletableObject) {
            logicalDeletableObject.setDeleted(true);
            EntityPersister entityPersister = event.getSession().getEntityPersister(event.getEntityName(), eventObject);
            EntityEntry entityEntry = event.getSession().getPersistenceContext().getEntry(eventObject);
            cascadeBeforeDelete(event.getSession(), entityPersister, entityEntry, transientEntities);

            cascadeAfterDelete(event.getSession(), entityPersister, eventObject, transientEntities);
        } else {
            super.onDelete(event, transientEntities);
        }
    }

}
