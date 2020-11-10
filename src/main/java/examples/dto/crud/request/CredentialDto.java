package examples.dto.crud.request;

import examples.domain.Credential;
import lombok.Data;
import personal.project.springinfra.annotation.validation.CascadeValidation;
import personal.project.springinfra.assets.ValidationGroups;
import personal.project.springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CredentialDto extends BaseCrudRequest<Credential, Long> {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @CascadeValidation(cascadeGroups = ValidationGroups.DynamicCrudValidationGroup.class)
    @NotNull(message = "user property is required")
    private UserDto user;
}
