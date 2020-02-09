package personal.project.springinfra.dto.crud;


import personal.project.springinfra.dto.BaseDto;
import personal.project.springinfra.model.domain.BaseDomain;

public abstract class BaseCrudRequest<E extends BaseDomain> extends BaseDto {

    public abstract E toEntity();

}
