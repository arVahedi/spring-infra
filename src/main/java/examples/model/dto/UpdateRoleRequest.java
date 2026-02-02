package examples.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springinfra.assets.AuthorityType;
import org.springinfra.model.dto.BaseRequest;

import java.util.List;

public record UpdateRoleRequest(
        @NotBlank(message = "Role name is required")
        String name,
        @NotEmpty(message = "AuthorityType is required")
        List<AuthorityType> authorities
) implements BaseRequest {
    public UpdateRoleRequest {
        authorities = (authorities == null) ? List.of() : List.copyOf(authorities);
    }
}
