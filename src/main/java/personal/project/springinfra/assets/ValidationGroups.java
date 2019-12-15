package personal.project.springinfra.assets;

import javax.validation.groups.Default;

public interface ValidationGroups {

    interface InsertValidationGroup extends Default {}
    interface UpdateValidationGroup extends Default{}
}
