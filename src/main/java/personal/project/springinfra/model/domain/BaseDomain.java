package personal.project.springinfra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public abstract class BaseDomain<ID extends Number> implements Persistable<ID> {

    private ID id;
    private Instant insertDate;
    private Instant lastModifiedDate;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.id == null || this.id.longValue() == 0;
    }

    @PrePersist
    public void fillInsertDate() {
        if (this.insertDate == null) {
            this.insertDate = Instant.now();
        }
    }

    @PreUpdate
    public void fillLastModifiedDate() {
        this.lastModifiedDate = Instant.now();
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

    @Basic
    @Column(name = "insert_date")
    public Instant getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
    }

    @Basic
    @Column(name = "last_modified_date")
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    //endregion

}
