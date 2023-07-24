package springinfra.model.dto.crud.request;


import lombok.Getter;
import lombok.Setter;
import springinfra.assets.ValidationGroups;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.BaseDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

@Getter
@Setter
public abstract class BaseCrudRequest<E extends BaseDomain<? extends Number>, I extends Number> extends BaseDto {
    @NotNull(message = "id is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Min(value = 1, message = "Minimum acceptable value for id is 1", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "id should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private I id;
    @NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "version should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private Long version;
}
