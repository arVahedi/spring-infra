package springinfra.model.converter;

import lombok.SneakyThrows;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.request.BaseCrudDto;

import java.lang.reflect.ParameterizedType;

public abstract class BaseCrudConverter<D extends BaseCrudDto, E extends BaseDomain> {

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
