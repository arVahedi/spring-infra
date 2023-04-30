package springinfra.assets;

import springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.groups.Default;

public interface ValidationGroups {

    class DynamicCrudValidationGroup implements AbstractiveValidationGroup {
        @Override
        public Class<?>[] getGroups(Object object) {
            if (object instanceof BaseCrudRequest baseCrudRequest) {
                if (baseCrudRequest.getId() == null) {
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
