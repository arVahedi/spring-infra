package personal.project.springinfra.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.dto.crud.request.BaseCrudRequest;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.BaseDomain;

import java.util.List;
import java.util.Optional;

public interface DefaultCrudService<E extends BaseDomain> extends CrudService<E> {

    @Transactional(rollbackFor = Exception.class)
    default E saveOrUpdate(BaseCrudRequest request) {
        E entity;
        if (request.getId() != null && request.getId().longValue() > 0) {    //This is update operation
            entity = find(request.getId().longValue());
            request.toEntity(entity);
        } else {    //This is insert operation
            entity = (E) request.toEntity(getGenericDomainClass());
            entity.setVersion(0L);
        }

        return (E) getRepository().save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    default E delete(long id) {
        E entity = find(id);
        getRepository().delete(entity);
        return entity;
    }

    default E find(long id) {
        Optional<E> optional = getRepository().findById(id);
        return optional.orElseThrow(() -> new NoSuchRecordException(String.format("%s with id [%d] doesn't exist", getGenericDomainClass().getSimpleName(), id)));
    }

    default List<E> findAll() {
        return getRepository().findAll();
    }

    @Slf4j
    class Logger {
    }
}
