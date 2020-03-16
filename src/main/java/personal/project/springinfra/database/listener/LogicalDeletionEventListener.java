package personal.project.springinfra.database.listener;

import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.persister.entity.EntityPersister;
import personal.project.springinfra.model.domain.LogicalDeletableDomain;

import java.util.Set;

public class LogicalDeletionEventListener extends DefaultDeleteEventListener {

    @Override
    public void onDelete(DeleteEvent event, Set arg1) {
        Object o = event.getObject();
        if (o instanceof LogicalDeletableDomain) {
            ((LogicalDeletableDomain) o).setDeleted(true);
            EntityPersister entityPersister = event.getSession().getEntityPersister(event.getEntityName(), o);
            EntityEntry entityEntry = event.getSession().getPersistenceContext().getEntry(o);
            cascadeBeforeDelete(event.getSession(), entityPersister, o, entityEntry, arg1);

            cascadeAfterDelete(event.getSession(), entityPersister, o, arg1);

        } else {
            super.onDelete(event, arg1);
        }
    }

}
