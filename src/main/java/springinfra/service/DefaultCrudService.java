package springinfra.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import springinfra.exception.NoSuchRecordException;
import springinfra.model.domain.BaseDomain;
import springinfra.model.domain.OptimisticLockableDomain;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import java.util.List;
import java.util.Optional;

public interface DefaultCrudService<E extends BaseDomain, I extends Number> extends CrudService<E, I> {

    @Transactional(rollbackFor = Exception.class)
    default E saveOrUpdate(BaseCrudRequest request) {
        E entity;
        if (request.getId() != null && request.getId().longValue() > 0) {    //This is update operation
            entity = find((I) request.getId());
            getCrudConverter().toEntity(request, entity);
        } else {    //This is insert operation
            entity = (E) getCrudConverter().toEntity(request, getGenericDomainClass());
            if (entity instanceof OptimisticLockableDomain) {
                ((OptimisticLockableDomain) entity).setVersion(0L);
            }
        }

        return (E) getRepository().save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    default E delete(I id) {
        E entity = find(id);
        getRepository().delete(entity);
        return entity;
    }

    default E find(I id) {
        Optional<E> optional = getRepository().findById(id);
        return optional.orElseThrow(() -> new NoSuchRecordException(String.format("%s with id [%d] doesn't exist", getGenericDomainClass().getSimpleName(), id)));
    }

    default List<E> list() {
        return getRepository().findAll();
    }

    default List<E> list(Pageable pageable) {
        return getRepository().findAll(pageable).toList();
    }

    @Slf4j
    class Logger {
    }
}
