package personal.project.springinfra.dto.generator;

import personal.project.springinfra.model.domain.BaseDomain;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultCrudApiResponseGenerator<E extends BaseDomain> implements CrudApiResponseGenerator<E> {

    @Override
    public Object onSave(E entity) {
        return this.onGeneral(entity);
    }

    @Override
    public Object onUpdate(E entity) {
        return this.onGeneral(entity);
    }

    @Override
    public Object onFind(E entity) {
        return this.onGeneral(entity);
    }

    @Override
    public List<Object> onList(List<E> entityList) {
        return entityList.stream().map(this::onFind).collect(Collectors.toList());
    }

    @Override
    public Object onDelete(E entity) {
        return entity.getId();
    }

    @Override
    public Object onGeneral(E entity) {
        return entity;
    }
}
