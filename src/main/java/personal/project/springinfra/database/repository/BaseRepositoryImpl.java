package personal.project.springinfra.database.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.model.domain.BaseDomain;

import javax.persistence.EntityManager;

@NoRepositoryBean
@Transactional
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
}
