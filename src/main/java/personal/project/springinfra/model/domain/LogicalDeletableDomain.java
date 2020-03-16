package personal.project.springinfra.model.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostRemove;

@MappedSuperclass
public abstract class LogicalDeletableDomain<ID extends Number> extends BaseDomain<ID> {

    private boolean deleted;

    @PostRemove
    public void preRemoveAction() {
        this.deleted = true;
    }

    //region Getter and Setter
    @Basic
    @Column(name = "deleted")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    //endregion
}
