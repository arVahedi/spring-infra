package org.springinfra.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springinfra.model.domain.BaseDomain;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<E extends BaseDomain> extends JpaRepository<E, Long> {

    void detach(E entity);
}
