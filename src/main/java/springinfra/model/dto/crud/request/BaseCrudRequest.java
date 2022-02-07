package springinfra.model.dto.crud.request;


import lombok.Data;
import springinfra.assets.ValidationGroups;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.BaseDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public abstract class BaseCrudRequest<E extends BaseDomain, ID extends Number> extends BaseDto {
    @NotNull(message = "id is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Min(value = 1, message = "Minimum acceptable value for id is 1", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "id should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private ID id;
    @NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "version should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private Long version;
}
