package personal.project.springinfra.assets;

import javax.validation.groups.Default;

public interface ValidationGroups {

    interface InsertGroup extends Default {}
    interface UpdateGroup extends Default{}
}
