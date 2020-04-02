package personal.project.springinfra.dto.crud;

import lombok.Data;
import personal.project.springinfra.annotation.validation.CascadeValidate;
import personal.project.springinfra.assets.Regex;
import personal.project.springinfra.assets.status.UserStatus;
import personal.project.springinfra.model.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDto extends BaseCrudRequest<User, Long> {
    @NotBlank(message = "First-Name is required")
    private String firstName;
    @NotBlank(message = "Last-Name is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = Regex.EMAIL, message = "Email format is wrong")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "Phone number format is wrong")
    private String phoneNumber;
    @NotNull(message = "status is required")
    private UserStatus status;

    @Override
    public User toEntity(User user) {
        user.setId(getId());
        user.setVersion(getVersion());
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPhone(this.phoneNumber);
        user.setStatus(this.status);
        return user;
    }
}
