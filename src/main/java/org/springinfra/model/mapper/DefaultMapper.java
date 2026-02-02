package org.springinfra.model.mapper;

import org.mapstruct.InheritConfiguration;
import org.springinfra.model.entity.BaseEntity;

import java.util.List;

/**
 * A parent for all mappers.
 * <p>
 * A crud mapper is a mapper which is responsible to create views from entities
 *
 * @param <E> the target entity (basically a child of {@link BaseEntity})
 * @param <V> the target view (Service method returns this object)
 */

public interface DefaultMapper<E extends BaseEntity, V> {

    V toView(E entity);

    @InheritConfiguration(name = "toView")
    List<V> toViews(List<E> entities);
}
