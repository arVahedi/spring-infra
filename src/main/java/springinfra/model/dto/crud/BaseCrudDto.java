package springinfra.model.dto.crud;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import springinfra.assets.ValidationGroups;
import springinfra.model.domain.BaseDomain;
import springinfra.model.dto.BaseDto;

import java.time.Instant;

/**
 * This DTO is presented as the parent of all crud DTOs.
 * A crud DTO is a DTO class that completely corresponds to an entity class.
 *
 * @param <E> The corresponding entity class. (Please consider this parameter is not used so far apparently, but it's not
 *            redundant, it's used as a reference check of I to make sure the type of identifier of entity and DTO are the same)
 * @param <I> The Identifier type of the corresponding entity class in order to be used as the same identifier type at DTO
 */

@Getter
@Setter
public abstract class BaseCrudDto<E extends BaseDomain<I>, I extends Number> extends BaseDto {
    @Null(message = "id should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private I id;
    @NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "version should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private Long version;
    @Null(message = "insertDate is not allowed to have value, it should be null")
    private Instant insertDate;
    @Null(message = "lastModifiedDate is not allowed to have value, it should be null")
    private Instant lastModifiedDate;
}
