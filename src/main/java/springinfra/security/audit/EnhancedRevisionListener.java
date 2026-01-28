package springinfra.security.audit;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.RevisionListener;
import springinfra.model.domain.audit.AuditedRevision;
import springinfra.utility.identity.IdentityUtility;

import java.util.Date;

/**
 * this is custom revision listener of Hibernate, which handles filling out details of {@link org.hibernate.envers.RevisionEntity}
 * in audit events.
 *
 * @see org.hibernate.envers.RevisionEntity
 */

@Slf4j
public class EnhancedRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        if (revisionEntity instanceof AuditedRevision auditedRevision) {
            auditedRevision.setOccurrenceDate(new Date());
            auditedRevision.setPrincipal(IdentityUtility.getUsername().orElse(null));
        } else {
            log.warn("An unhandled revision entity has been triggered, its type is {} instead of expected type {}", revisionEntity.getClass().getName(), AuditedRevision.class.getName());
        }
    }
}
