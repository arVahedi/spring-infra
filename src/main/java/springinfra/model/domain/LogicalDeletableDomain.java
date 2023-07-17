package springinfra.model.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostRemove;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@MappedSuperclass
@Audited
public abstract class LogicalDeletableDomain<ID extends Number> extends BaseDomain<ID> {

    @Basic
    @Column(name = "deleted")
    private boolean deleted;

    @PostRemove
    public void preRemoveAction() {
        this.deleted = true;
    }
}
