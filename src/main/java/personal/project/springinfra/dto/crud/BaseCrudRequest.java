package personal.project.springinfra.dto.crud;


import lombok.Data;
import lombok.SneakyThrows;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.dto.BaseDto;
import personal.project.springinfra.model.domain.BaseDomain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;

@Data
public abstract class BaseCrudRequest<E extends BaseDomain> extends BaseDto {
    @NotNull(message = "id is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Min(value = 1, message = "Minimum acceptable value for id is 1", groups = ValidationGroups.UpdateValidationGroup.class)
    private Long id;
    @NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)
    private Long version;

    @SneakyThrows
    public final E toEntity() {
        E entity = ((Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]).getConstructor().newInstance();
        return this.toEntity(entity);
    }

    public abstract E toEntity(E entity);

}
