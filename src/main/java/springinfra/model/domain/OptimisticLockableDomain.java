package springinfra.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class OptimisticLockableDomain<ID extends Number> extends BaseDomain<ID> {

    @Version
    @Column(name = "version")
    private Long version;

}
