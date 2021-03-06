package personal.project.springinfra.model.converter;

import lombok.SneakyThrows;
import personal.project.springinfra.model.domain.BaseDomain;
import personal.project.springinfra.model.dto.crud.request.BaseCrudRequest;

import java.lang.reflect.ParameterizedType;

public abstract class BaseCrudConverter<D extends BaseCrudRequest, E extends BaseDomain> {

    @SneakyThrows
    public E toEntity(D dto, Class<E> domainClass) {
        E entity = domainClass.getConstructor().newInstance();
        return this.toEntity(dto, entity);
    }

    public abstract E toEntity(D dto, E entity);

    private Class<E> getGenericDomainClassType() {
        return (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }
}
