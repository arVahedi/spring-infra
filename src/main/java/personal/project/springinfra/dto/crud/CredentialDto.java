package personal.project.springinfra.dto.crud;

import lombok.Data;
import personal.project.springinfra.model.domain.Credential;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class CredentialDto extends BaseCrudRequest<Credential, Long> {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @Valid
    private UserDto user;

    @Override
    public Credential toEntity(Credential entity) {
        entity.setUsername(this.username);
        entity.changePassword(this.password);
        entity.setUser(user.toEntity());
        return entity;
    }
}
