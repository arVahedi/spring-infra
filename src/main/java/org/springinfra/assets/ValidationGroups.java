package org.springinfra.assets;

import jakarta.validation.groups.Default;
import org.springinfra.model.dto.crud.BaseCrudDto;

/**
 * This interface holds all validation groups that we want to use including both simple groups and {@link AbstractiveValidationGroup}
 */

public interface ValidationGroups {

    /**
     * This is an abstractive validation group that makes decision about the validation group of crud objects, according to their id field.
     * If their id field is filled out, means we are doing update operation, so the validation group of validating object should be {@link UpdateValidationGroup},
     * otherwise it's a insert operation and the group should set to {@link InsertValidationGroup}.
     * It can be used for validating inner crud object of another nested curd object. Imagine class User and Credential. Each credential object has an inner user object.
     * Now, in the credential controller, we now which operation is being done on the credential object itself (based on type of HTTP request, POST or PUT),
     * but regardless the type of credential operation, each credential can be linked to a new user (means we would have and insert operation for it) or be linked to existing user (means we would have update operation for user).
     * In that case we need to indicate how user object should be validated, with InsertValidationGroup or UpdateValidationGroup and as we said, it's independent of type of credential operation.
     * For handling this kind of use-cases, we use this validation group.
     */
    class DynamicCrudValidationGroup implements AbstractiveValidationGroup {
        @Override
        public Class<?>[] getGroups(Object object) {
            if (object instanceof BaseCrudDto baseCrudDto) {
                if (baseCrudDto.getId() == null) {
                    return new Class[]{InsertValidationGroup.class};
                } else {
                    return new Class[]{UpdateValidationGroup.class};
                }
            }
            return new Class[0];
        }
    }

    interface InsertValidationGroup extends Default {
    }

    interface UpdateValidationGroup extends Default {
    }
}
