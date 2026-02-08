package org.springinfra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springinfra.model.entity.BaseEntity;

@NoRepositoryBean
public interface EnhancedJpaRepository<E extends BaseEntity> extends JpaRepository<E, Long> {

}
