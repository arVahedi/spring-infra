package springinfra.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import springinfra.database.repository.BaseRepository;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.BaseCrudDto;
import springinfra.model.mapper.BaseCrudMapper;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * This is a default and basic implementation of {@link CrudService}
 *
 * @param <I> the identity of entity and crud DTO object (both should have the same identity type)
 * @param <E> the target entity class (usually a child of {@link BaseDomain} class
 * @param <D> the target crud DTO class (usually a child of {@link BaseCrudDto} class
 */

public interface DefaultCrudService<I extends Number, E extends BaseDomain<I>, D extends BaseCrudDto<E, I>> extends CrudService<I, E, D> {

    @Transactional(rollbackFor = Exception.class)
    default E save(D request) {
        E entity = getMapper().toEntity(request);
        return getRepository().save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    default E update(I id, D request) {
        E entity = find(id);
        getMapper().updateEntity(entity, request);
        return getRepository().save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    default E delete(I id) {
        E entity = find(id);
        getRepository().delete(entity);
        return entity;
    }

    default E find(I id) {
        Optional<E> optional = getRepository().findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("The entity with id [{0}] does not exist", id)));
    }

    default List<E> list() {
        return getRepository().findAll();
    }

    default List<E> list(Pageable pageable) {
        return getRepository().findAll(pageable).toList();
    }

    BaseRepository<E, I> getRepository();

    BaseCrudMapper<E, D> getMapper();

    @Slf4j
    class Logger {
    }
}
