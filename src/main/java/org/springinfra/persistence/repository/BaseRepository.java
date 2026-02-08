package org.springinfra.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springinfra.model.entity.BaseEntity;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
@Transactional(readOnly = true)
public interface BaseRepository<E extends BaseEntity> extends EnhancedJpaRepository<E> {
    Optional<E> findByPublicId(UUID pId);

    void deleteByPublicId(UUID pId);
}
