package org.springinfra.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * The parent of all entity classes. As you can see all entities will have an {@link #id}, {@link #createdDate},
 * and {@link #lastModifiedDate}.
 *
 * @param <I> the type of identity of entity which should be a subclass of {@link Number}
 */

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public abstract class BaseDomain<I extends Number> implements Persistable<I>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private I id;

    @Basic
    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @Basic
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.id == null || this.id.longValue() == 0;
    }
}
