package examples.dto.crud;

import examples.domain.Role;
import lombok.Getter;
import lombok.Setter;
import org.springinfra.assets.AuthorityType;
import org.springinfra.model.dto.crud.BaseCrudDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class RoleDto extends BaseCrudDto<Role, Integer> {
    @NotBlank(message = "Role name is required")
    private String name;
    @NotEmpty(message = "AuthorityType is required")
    private List<AuthorityType> authorities;
}
