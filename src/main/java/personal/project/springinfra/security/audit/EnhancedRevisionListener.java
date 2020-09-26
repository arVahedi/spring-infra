package personal.project.springinfra.security.audit;

import org.hibernate.envers.RevisionListener;
import personal.project.springinfra.model.domain.audit.AuditedRevision;

import java.util.Date;

public class EnhancedRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditedRevision auditedRevision = (AuditedRevision) revisionEntity;
        auditedRevision.setOccurrenceDate(new Date());

        // TODO: 4/13/2020 AD fill user id (https://stackoverflow.com/questions/35433598/hibernate-envers-include-date-of-when-change-happened)
    }
}
