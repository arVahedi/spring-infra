package examples.dto.crud;

import examples.domain.Credential;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springinfra.annotation.validation.GroupBasedValidation;
import org.springinfra.annotation.validation.SecureProperty;
import org.springinfra.assets.ValidationGroups;
import org.springinfra.model.dto.crud.BaseCrudDto;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import static org.springinfra.assets.AuthorityType.ROLE_MANAGEMENT_AUTHORITY;
import static org.springinfra.assets.Constant.PASSWORD_MASK;

@Getter
@Setter
public class CredentialDto extends BaseCrudDto {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @GroupBasedValidation(ValidationGroups.DynamicCrudValidationGroup.class)
    @NotNull(message = "user property is required")
    private UserDto user;
    @SecureProperty(accessibleFor = ROLE_MANAGEMENT_AUTHORITY, message = "roles property doesn't exist")
    private List<Long> roles = new ArrayList<>();

    @Transient
    @AssertFalse(message = "Password value is not valid", groups = ValidationGroups.InsertValidationGroup.class)
    public boolean isPasswordMask() {
        return PASSWORD_MASK.equalsIgnoreCase(this.password);
    }
}
