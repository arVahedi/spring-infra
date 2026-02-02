package examples.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.springinfra.annotation.validation.SecureProperty;
import org.springinfra.model.dto.BaseRequest;

import java.util.List;
import java.util.UUID;

import static org.springinfra.assets.AuthorityType.ROLE_MANAGEMENT_AUTHORITY;

public record UpdateCredentialRequest(
        @NotBlank(message = "password is required")
        String password,
        @SecureProperty(accessibleFor = ROLE_MANAGEMENT_AUTHORITY, message = "roles property doesn't exist")
        List<UUID> roles
) implements BaseRequest {
    public UpdateCredentialRequest {
        roles = (roles == null) ? List.of() : List.copyOf(roles);
    }
}
