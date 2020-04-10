package personal.project.springinfra.database.repository;

import lombok.Getter;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.model.domain.BaseDomain;

import javax.persistence.EntityManager;

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
        S result = super.save(entity);
        this.entityManager.flush();
        return result;
    }
}
