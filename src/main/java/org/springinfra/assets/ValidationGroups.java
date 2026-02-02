package org.springinfra.assets;

import jakarta.validation.groups.Default;

/**
 * This interface holds all validation groups that we want to use including both simple groups and {@link AbstractiveValidationGroup}
 *
 * <p>
 * <p>
 * Usage e.g.:
 * <br>
 * {@literal @}NotNull(message = "version is required", groups = ValidationGroups.UpdateValidationGroup.class)<br>
 * {@literal @}Null(message = "version should be null for insert operation", groups = ValidationGroups.InsertValidationGroup.class)<br>
 * private String field;
 */

public interface ValidationGroups {

    interface InsertValidationGroup extends Default {
    }

    interface UpdateValidationGroup extends Default {
    }
}
