package personal.project.springinfra.dto.crud.request;

import lombok.Data;
import personal.project.springinfra.annotation.validation.CascadeValidate;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.model.domain.BaseDomain;
import personal.project.springinfra.model.domain.Credential;
import personal.project.springinfra.model.domain.OptimisticLockableDomain;
import personal.project.springinfra.model.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CredentialDto extends BaseCrudRequest<Credential, Long> {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @CascadeValidate(cascadeGroups = ValidationGroups.DynamicCrudValidationGroup.class)
    @NotNull(message = "user property is required")
    private UserDto user;

    @Override
    public Credential toEntity(Credential credential) {
        credential.setId(getId());
        credential.setUsername(this.username);
        credential.changePassword(this.password);
        if (credential.getUser() == null) {
            credential.setUser(user.toEntity(User.class));
        } else {
            credential.setUser(user.toEntity(credential.getUser()));
        }

        return credential;
    }
}
