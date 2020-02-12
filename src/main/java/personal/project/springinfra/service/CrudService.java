package personal.project.springinfra.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import personal.project.springinfra.dto.crud.BaseCrudRequest;
import personal.project.springinfra.exception.NoSuchRecordException;
import personal.project.springinfra.model.domain.BaseDomain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public interface CrudService<E extends BaseDomain, D extends BaseCrudRequest> {



    @Transactional(rollbackFor = Exception.class)
    default E saveOrUpdate(D request) {
        E entity = null;
        try {
            entity = ((Class<E>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0]).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        if (request.getId() != null && request.getId() > 0) { //This is update operation
            E finalEntity = entity;
            entity = (E) getRepository().findById(request.getId()).orElseThrow(() -> new NoSuchRecordException(String.format("%s with id [%d] doesn't exist",
                    finalEntity.getClass().getSimpleName(), request.getId())));
            request.toEntity(entity);
        } else {    //This is insert operation
            entity = (E) request.toEntity();
            entity.setVersion(0L);
        }

        return (E) getRepository().save(entity);
    }

    JpaRepository getRepository();

    @Slf4j
    class Logger{}
}
