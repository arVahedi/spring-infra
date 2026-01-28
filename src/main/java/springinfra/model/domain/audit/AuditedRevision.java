package springinfra.model.domain.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import springinfra.security.audit.EnhancedRevisionListener;

import java.util.Date;

/**
 * This is an audited revision info entity for all <strong>audited</strong> entities which holds the meta-data of details about
 * changes of audited entities, such as who has changed the data or when the data has been changed, etc.
 * All _audit_log tables of audited entities will have a foreign key to this table for storing its own meta-data.
 *
 * @see EnhancedRevisionListener
 */

@Entity
@Table(name = "revision_info")
@RevisionEntity(EnhancedRevisionListener.class)
@Getter
@Setter
public class AuditedRevision {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private int id;

    @Basic
    @Column(name = "occurrence_date")
    @RevisionTimestamp
    private Date occurrenceDate;

    @Basic
    @Column(name = "principal")
    private String principal;
}
