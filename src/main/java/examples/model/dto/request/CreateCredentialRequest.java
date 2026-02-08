package examples.model.dto.request;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springinfra.annotation.validation.SecureProperty;
import org.springinfra.model.dto.request.BaseRequest;

import java.util.List;
import java.util.UUID;

import static org.springinfra.assets.AuthorityType.ROLE_MANAGEMENT_AUTHORITY;
import static org.springinfra.assets.Constant.PASSWORD_MASK;

public record CreateCredentialRequest(
        @NotBlank(message = "username is required")
        String username,
        @NotBlank(message = "password is required")
        String password,
        @NotNull(message = "user property is required")
        CreateUserRequest user,
        @SecureProperty(accessibleFor = ROLE_MANAGEMENT_AUTHORITY, message = "roles property doesn't exist")
        List<UUID> roles
) implements BaseRequest {
    public CreateCredentialRequest {
        roles = (roles == null) ? List.of() : List.copyOf(roles);
    }

    @AssertFalse(message = "Password value is not valid")
    public boolean isPasswordMask() {
        return PASSWORD_MASK.equalsIgnoreCase(this.password);
    }
}
