package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.model.domain.BaseDomain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public abstract class BaseCustomRepository<T extends BaseDomain, ID extends Serializable> {

    @PersistenceContext
    private EntityManager entityManager;

    //region Getter and Setter
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    //endregion
}
