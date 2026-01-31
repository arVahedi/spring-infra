package org.springinfra.service;

import org.springframework.data.domain.Pageable;
import org.springinfra.model.domain.BaseDomain;
import org.springinfra.model.dto.crud.BaseCrudDto;

import java.util.List;

/**
 * This is the interface of services that want to support CRUD operation. So all these methods should be supported by them.
 *
 * @param <E> the target entity class (usually a child of {@link BaseDomain} class
 * @param <D> the target crud DTO class (usually a child of {@link BaseCrudDto} class
 * @see DefaultCrudService
 */

public interface CrudService<E extends BaseDomain, D extends BaseCrudDto> {

    E save(D request);

    E update(Long id, D request);

    E delete(Long id);

    E find(Long id);

    List<E> list();

    List<E> list(Pageable pageable);

}
