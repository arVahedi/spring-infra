package org.springinfra.database.repository.custom;

import lombok.Getter;
import lombok.Setter;
import org.springinfra.model.domain.BaseDomain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Getter
@Setter
public abstract class BaseCustomRepository<E extends BaseDomain> {

    @PersistenceContext
    private EntityManager entityManager;

}
