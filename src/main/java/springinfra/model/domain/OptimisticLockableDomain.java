package springinfra.model.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class OptimisticLockableDomain<ID extends Number> extends BaseDomain<ID> {

    private Long version;

    //region Getter and Setter
    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    //endregion

}
