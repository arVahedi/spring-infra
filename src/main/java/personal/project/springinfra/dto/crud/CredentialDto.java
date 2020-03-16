package personal.project.springinfra.dto.crud;

import lombok.Data;
import personal.project.springinfra.model.domain.Credential;
import personal.project.springinfra.model.domain.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CredentialDto extends BaseCrudRequest<Credential, Long> {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @Valid
    @NotNull(message = "User property is required")
    private UserDto user;

    @Override
    public Credential toEntity(Credential credential) {
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
