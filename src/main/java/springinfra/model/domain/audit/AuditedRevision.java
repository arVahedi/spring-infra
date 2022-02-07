package springinfra.model.domain.audit;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import springinfra.security.audit.EnhancedRevisionListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "revision_info")
@RevisionEntity(EnhancedRevisionListener.class)
public class AuditedRevision {

    private int id;
    private Date occurrenceDate;
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
    public Date getOccurrenceDate() {
        return occurrenceDate;
    }

    public void setOccurrenceDate(Date occurrenceDate) {
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
