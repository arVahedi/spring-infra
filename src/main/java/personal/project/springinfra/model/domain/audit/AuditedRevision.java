package personal.project.springinfra.model.domain.audit;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import personal.project.springinfra.security.audit.EnhancedRevisionListener;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "revision_info")
@RevisionEntity(EnhancedRevisionListener.class)
public class AuditedRevision {

    private int id;
    private Instant occurrenceDate;
    private String userInfo;

    //region Getter and Setter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "occurrence_date")
    @RevisionTimestamp
    public Instant getOccurrenceDate() {
        return occurrenceDate;
    }

    public void setOccurrenceDate(Instant occurrenceDate) {
        this.occurrenceDate = occurrenceDate;
    }

    @Basic
    @Column(name = "user_info")
    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userId) {
        this.userInfo = userId;
    }
    //endregion
}
