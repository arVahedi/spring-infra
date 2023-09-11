package springinfra.model.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.MappingTarget;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.crud.BaseCrudDto;

import java.util.List;

/**
 * A parent for all crud mappers.
 * <p>
 * A crud mapper is a mapper which is responsible to handle mapping of an entity to a {@link BaseCrudDto crud DTO} and vice versa
 *
 * @param <E> the target entity (basically a child of {@link BaseDomain})
 * @param <D> the target DTO (basically a child of {@link BaseCrudDto})
 */

public interface BaseCrudMapper<E extends BaseDomain<?>, D extends BaseCrudDto<E, ?>> {

    E toEntity(D dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(@MappingTarget E entity, D dto);

    D toDto(E entity);

    List<E> toEntities(List<D> dtos);

    List<D> toDtos(List<E> entities);
}
