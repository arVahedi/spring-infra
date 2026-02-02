package org.springinfra.model.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.MappingTarget;
import org.springinfra.model.dto.BaseRequest;
import org.springinfra.model.entity.BaseEntity;
import org.springinfra.utility.mapping.ObjectMapperUtil;

import java.util.Map;

/**
 * A parent for all CRUD mappers.
 * <p>
 * A crud mapper is a mapper that handles essentials mappings for CRUD endpoints (supporting mapping of create, update and patch)
 *
 * @param <C> the <storng>create request</storng> DTO (basically a child of {@link BaseRequest})
 * @param <U> the <strong>update request</strong> DTO (basically a child of {@link BaseRequest})
 * @param <E> the target entity (basically a child of {@link BaseEntity})
 * @param <V> the target view (Service method returns this object)
 */
public interface CrudMapper<
        C extends BaseRequest,
        U extends BaseRequest,
        E extends BaseEntity,
        V> extends DefaultMapper<E, V> {

    E toEntity(C request);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(@MappingTarget E entity, U request);

    default void patchEntity(@MappingTarget E entity, Map<String, Object> properties) {
        ObjectMapperUtil.applyMapToObject(properties, entity);
    }
}
