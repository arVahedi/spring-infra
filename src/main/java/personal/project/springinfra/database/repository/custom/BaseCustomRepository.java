package personal.project.springinfra.database.repository.custom;

import personal.project.springinfra.model.domain.BaseDomain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseCustomRepository<E extends BaseDomain> {

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
