package examples.dto.crud.request;

import examples.domain.Credential;
import lombok.Getter;
import lombok.Setter;
import springinfra.annotation.validation.GroupBasedValidation;
import springinfra.assets.ValidationGroups;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CredentialDto extends BaseCrudRequest<Credential, Long> {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;
    @GroupBasedValidation(ValidationGroups.DynamicCrudValidationGroup.class)
    @NotNull(message = "user property is required")
    private UserDto user;
    private List<Integer> roles = new ArrayList<>();
}
