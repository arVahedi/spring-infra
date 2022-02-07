package springinfra.database.repository.custom;

import lombok.Getter;
import lombok.Setter;
import springinfra.model.domain.BaseDomain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Getter
@Setter
public abstract class BaseCustomRepository<E extends BaseDomain> {

    @PersistenceContext
    private EntityManager entityManager;

}
