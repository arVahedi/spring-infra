package org.springinfra.persistence.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.model.entity.BaseEntity;
import org.springinfra.utility.persistence.PublicIdUtil;

@NoRepositoryBean
@Transactional(readOnly = true)
public class EnhancedJpaRepositoryImpl<E extends BaseEntity> extends SimpleJpaRepository<E, Long>
        implements EnhancedJpaRepository<E> {

    public EnhancedJpaRepositoryImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public EnhancedJpaRepositoryImpl(Class<E> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    @Override
    public <S extends E> S save(S entity) {
        if (entity.getPublicId() == null) {
            entity.setPublicId(PublicIdUtil.generate());
        }
        return super.save(entity);
    }

    @Override
    public <S extends E> S saveAndFlush(S entity) {
        if (entity.getPublicId() == null) {
            entity.setPublicId(PublicIdUtil.generate());
        }
        return super.saveAndFlush(entity);
    }
}
