package springinfra.database.repository;

import lombok.Getter;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import springinfra.model.domain.BaseDomain;

import jakarta.persistence.EntityManager;

@NoRepositoryBean
@Transactional
@Getter
public class BaseRepositoryImpl<E extends BaseDomain, ID> extends SimpleJpaRepository<E, ID> implements BaseRepository<E, ID> {

    private EntityManager entityManager;

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
        Assert.notNull(entity, "Entity must not be null.");

        S attachedEntity = this.entityManager.merge(entity);
        // We clone the attached entity (is attached by merge method in above line), for returning from this method as result, because any object that is returned from repositories methods will be detached automatically by RepositoryAspect class.
        // So if we return the attached entity (not his clone), it will be detached and actually entity manager doesn't persist it on database when transaction is closed.
        // The another way for handling this situation is flush the EntityManager before returning the attached entity from this method, but it can harm performance. So we prefer to use clone approach though it still can be an expensive operation.
        entity = SerializationUtils.clone(attachedEntity);

        return entity;
    }
}
