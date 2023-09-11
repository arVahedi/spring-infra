package springinfra.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class OptimisticLockableDomain<I extends Number> extends BaseDomain<I> {

    @Version
    @Column(name = "version")
    private long version;

}
