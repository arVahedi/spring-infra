package examples.dto.crud;

import examples.assets.UserStatus;
import examples.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import springinfra.assets.Regex;
import springinfra.model.dto.crud.BaseCrudDto;

@Getter
@Setter
public class UserDto extends BaseCrudDto<User, Long> {
    @NotBlank(message = "First-Name is required")
    private String firstName;
    @NotBlank(message = "Last-Name is required")
    private String lastName;
    @Pattern(regexp = Regex.EMAIL, message = "Email format is wrong")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "Phone number format is wrong")
    private String phone;
    @NotNull(message = "status is required")
    private UserStatus status;
}
