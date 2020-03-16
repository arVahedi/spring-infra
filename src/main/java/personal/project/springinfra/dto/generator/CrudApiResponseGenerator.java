package personal.project.springinfra.dto.generator;

import personal.project.springinfra.model.domain.BaseDomain;

import java.util.List;

public interface CrudApiResponseGenerator<E extends BaseDomain> {

    Object onSave(E entity);

    Object onUpdate(E entity);

    Object onDelete(E entity);

    Object onFind(E entity);

    Object onList(List<E> entityList);

    Object onGeneral(E entity);
}
