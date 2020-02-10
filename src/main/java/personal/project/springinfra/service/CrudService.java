package personal.project.springinfra.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.dto.crud.BaseCrudRequest;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.BaseDomain;

import java.lang.reflect.ParameterizedType;

public interface CrudService<E extends BaseDomain> {

    @Transactional(rollbackFor = Exception.class)
    default E saveOrUpdate(BaseCrudRequest request) {
        E entity = ((Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getConstructor().newInstance();;
        if (request.getId() != null && request.getId() > 0) { //This is update operation
            entity = getRepository().findById(request.getId()).orElseThrow(() -> new NoSuchRecordException(String.format("%s with id [%d] doesn't exist",
                    entity.getClass().getSimpleName(), request.getId())));
            request.toEntity(entity);
        } else {    //This is insert operation
            entity = (E) request.toEntity();
            entity.setVersion(0L);
        }

        return (E) getRepository().save(entity);
    }

    CrudRepository getRepository();
}
