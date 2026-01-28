package springinfra.model.domain;

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
 * @param <I> the type of identity of entity which should be a subclass of {@link Number}
 */

@Getter
@Setter
@MappedSuperclass
public abstract class OptimisticLockableDomain<I extends Number> extends BaseDomain<I> {

    @Version
    @Column(name = "version")
    private long version;

}
