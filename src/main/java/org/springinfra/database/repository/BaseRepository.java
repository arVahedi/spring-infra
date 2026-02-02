package org.springinfra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springinfra.model.entity.BaseEntity;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {

    Optional<E> findByPublicId(UUID pId);

    void deleteByPublicId(UUID pId);
}
