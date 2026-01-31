package org.springinfra.model.dto.crud;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.springinfra.assets.ValidationGroups;
import org.springinfra.model.dto.BaseDto;

import java.time.Instant;

/**
 * This DTO is presented as the parent of all crud DTOs.
 * A crud DTO is a DTO class that completely corresponds to an entity class.
 *
 */

@Getter
@Setter
public abstract class BaseCrudDto extends BaseDto {
    @Null(message = "id should be null for insert/update operation (use path variable instead)")
    private Long id;
    @NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)
    @Null(message = "version should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)
    private Long version;
    @Null(message = "createdDate is not allowed to have value, it should be null")
    private Instant createdDate;
    @Null(message = "lastModifiedDate is not allowed to have value, it should be null")
    private Instant lastModifiedDate;
}
