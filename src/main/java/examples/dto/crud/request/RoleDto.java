package examples.dto.crud.request;

import examples.domain.Role;
import lombok.Data;
import springinfra.assets.AuthorityType;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RoleDto extends BaseCrudRequest<Role, Integer> {
    @NotBlank(message = "Role name is required")
    private String name;
    @NotEmpty(message = "AuthorityType is required")
    private List<AuthorityType> authorities;
}
