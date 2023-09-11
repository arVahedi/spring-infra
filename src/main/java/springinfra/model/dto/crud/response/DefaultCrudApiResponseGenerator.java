package springinfra.model.dto.crud.response;

import springinfra.model.domain.BaseDomain;

import java.util.List;

public class DefaultCrudApiResponseGenerator<E extends BaseDomain<? extends Number>> implements CrudApiResponseGenerator<E> {

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
        return entityList.stream().map(this::onFind).toList();
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
