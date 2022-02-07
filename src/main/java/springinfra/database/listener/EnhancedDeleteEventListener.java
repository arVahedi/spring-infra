package springinfra.database.listener;

import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;
import springinfra.model.domain.LogicalDeletableDomain;

import java.util.Set;

@Component
public class EnhancedDeleteEventListener extends DefaultDeleteEventListener {

    @Override
    public void onDelete(DeleteEvent event, Set arg1) {
        Object eventObject = event.getObject();
        if (eventObject instanceof LogicalDeletableDomain) {
            ((LogicalDeletableDomain) eventObject).setDeleted(true);
            EntityPersister entityPersister = event.getSession().getEntityPersister(event.getEntityName(), eventObject);
            EntityEntry entityEntry = event.getSession().getPersistenceContext().getEntry(eventObject);
            cascadeBeforeDelete(event.getSession(), entityPersister, eventObject, entityEntry, arg1);

            cascadeAfterDelete(event.getSession(), entityPersister, eventObject, arg1);

        } else {
            super.onDelete(event, arg1);
        }
    }

}
