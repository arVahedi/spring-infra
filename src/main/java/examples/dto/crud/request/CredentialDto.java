package examples.dto.crud.request;

import examples.domain.Credential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import springinfra.annotation.validation.GroupBasedValidation;
import springinfra.assets.ValidationGroups;
import springinfra.model.dto.crud.request.BaseCrudDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CredentialDto extends BaseCrudDto<Credential, Long> {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @GroupBasedValidation(ValidationGroups.DynamicCrudValidationGroup.class)
    @NotNull(message = "user property is required")
    private UserDto user;
    private List<Integer> roles = new ArrayList<>();
}
