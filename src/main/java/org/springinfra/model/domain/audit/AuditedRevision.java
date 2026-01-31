package org.springinfra.model.domain.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.springinfra.security.audit.EnhancedRevisionListener;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revision_info_seq")
    @SequenceGenerator(
            name = "revision_info_seq",
            sequenceName = "revision_info_id_seq",
            allocationSize = 50)
    @Column(name = "id")
    @RevisionNumber
    private Long id;

    @Basic
    @Column(name = "occurrence_date")
    @RevisionTimestamp
    private Date occurrenceDate;

    @Basic
    @Column(name = "principal")
    private String principal;
}
