package personal.project.springinfra.assets;

import personal.project.springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.groups.Default;

public interface ValidationGroups {

    class DynamicCrudValidationGroup extends VirtualValidationGroups {
        @Override
        public Class<?>[] actualGroups(Object object) {
            if (object instanceof BaseCrudRequest) {
                if (((BaseCrudRequest) object).getId() == null) {
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
