package springinfra.model.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.MappingTarget;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.request.BaseCrudDto;

import java.util.List;

public interface BaseCrudMapper<E extends BaseDomain<?>, D extends BaseCrudDto<E, ?>> {

    E toEntity(D dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(@MappingTarget E entity, D dto);

    D toDto(E entity);

    List<E> toEntities(List<D> dtos);

    List<D> toDtos(List<E> entities);
}
