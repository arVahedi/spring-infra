package springinfra.model.domain.audit;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import springinfra.security.audit.EnhancedRevisionListener;

import jakarta.persistence.*;
import java.util.Date;

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
    @Column(name = "user_info")
    private String userInfo;
}
