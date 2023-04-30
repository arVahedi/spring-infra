package examples.dto.crud.request;

import examples.assets.UserStatus;
import examples.domain.User;
import lombok.Getter;
import lombok.Setter;
import springinfra.assets.Regex;
import springinfra.model.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserDto extends BaseCrudRequest<User, Long> {
    @NotBlank(message = "First-Name is required")
    private String firstName;
    @NotBlank(message = "Last-Name is required")
    private String lastName;
    @Pattern(regexp = Regex.EMAIL, message = "Email format is wrong")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "Phone number format is wrong")
    private String phoneNumber;
    @NotNull(message = "status is required")
    private UserStatus status;
}
