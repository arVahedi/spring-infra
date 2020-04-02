package personal.project.springinfra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public abstract class BaseDomain<ID extends Number> implements Persistable<ID> {

    private ID id;
    private Long version;
    private Date insertDate;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.id == null || this.id.longValue() == 0;
    }

    @PrePersist
    public void fillInsertDate() {
        if (this.insertDate == null) {
            this.insertDate = new Date();
        }
    }

    //region Getter and Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "insert_date", columnDefinition = "TIMESTAMP")
    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }
    //endregion

}
