package personal.project.springinfra.dto.crud;


import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.dto.BaseDto;
import personal.project.springinfra.model.domain.BaseDomain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.lang.reflect.ParameterizedType;

@Data
public abstract class BaseCrudRequest<E extends BaseDomain, ID extends Number> extends BaseDto {
    @NotNull(message = "id is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Min(value = 1, message = "Minimum acceptable value for id is 1", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "id should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private ID id;
    @NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "version should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private Long version;

    @SneakyThrows
    public final E toEntity(Class<E> domainClass) {
        E entity = domainClass.getConstructor().newInstance();
        return this.toEntity(entity);
    }

    public abstract E toEntity(E entity);

    private Class<E> getGenericDomainClassType() {
        return (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
