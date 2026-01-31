package org.springinfra.database.repository;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springinfra.model.domain.BaseDomain;

@NoRepositoryBean
@Transactional
@Getter
public class BaseRepositoryImpl<E extends BaseDomain> extends SimpleJpaRepository<E, Long> implements BaseRepository<E> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public BaseRepositoryImpl(Class<E> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public void detach(E entity) {
        this.entityManager.detach(entity);
    }

    @Override
    public <S extends E> S save(S entity) {
        Assert.notNull(entity, "Entity must be non null.");

        S attachedEntity = this.entityManager.merge(entity);
        // We clone the attached entity (is attached by merge method in the above line), for returning from this method as a result because any object that is produced from repositories methods will be detached automatically by the RepositoryAspect class.
        // So if we return the attached entity (not his clone), it will be detached, and actually, the entity manager doesn't persist it on the database when the transaction is closed.
        // Another way to handle this situation is flushing the EntityManager before returning the attached entity from this method, but it can hurt performance. So we prefer to use the clone approach though it can still be expensive.
        entity = SerializationUtils.clone(attachedEntity);

        return entity;
    }
}
