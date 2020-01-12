package personal.project.springinfra.database.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseCustomRepository {

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
