package org.springinfra.model.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostRemove;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springinfra.database.listener.EnhancedDeleteEventListener;

/**
 * Any entity that wants to support logical deletion, must be inherited from this class.
 * In that case, a boolean field named {@link #deleted} is added to the entity automatically, as well as all delete operations
 * will be handled in the logical deletion way automatically, thanks to {@link EnhancedDeleteEventListener}.
 *
 * @param <I> the type of identity of entity which should be a subclass of {@link Number}
 * @see EnhancedDeleteEventListener
 */

@Getter
@Setter
@MappedSuperclass
@Audited
public abstract class LogicalDeletableDomain<I extends Number> extends BaseDomain<I> {

    @Basic
    @Column(name = "deleted")
    private boolean deleted;

    @PostRemove
    public void postRemoveAction() {
        this.deleted = true;
    }
}
