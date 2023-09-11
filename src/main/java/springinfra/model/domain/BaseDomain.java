package springinfra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public abstract class BaseDomain<I extends Number> implements Persistable<I>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private I id;

    @Basic
    @Column(name = "insert_date")
    private Instant insertDate;

    @Basic
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Override
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
}
