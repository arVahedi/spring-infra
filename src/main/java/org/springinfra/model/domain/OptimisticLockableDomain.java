package org.springinfra.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * This method provides a way for putting optimistic lock on any entity automatically.
 * In that case, a {@link #version} field will be added automatically to the entity that will be used by Hibernate to
 * control optimistic lock.
 *
 */

@Getter
@Setter
@MappedSuperclass
public abstract class OptimisticLockableDomain extends BaseDomain {

    @Version
    @Column(name = "version")
    private long version;

}
