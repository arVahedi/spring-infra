package examples.dto.crud.request;

import lombok.Data;
import personal.project.springinfra.assets.Regex;
import examples.assets.UserStatus;
import examples.domain.User;
import personal.project.springinfra.dto.crud.request.BaseCrudRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
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

    @Override
    public User toEntity(User user) {
        user.setId(getId());
        /*if (user instanceof OptimisticLockableDomain) {
            ((OptimisticLockableDomain) user).setVersion(getVersion());
        }*/
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPhone(this.phoneNumber);
        user.setStatus(this.status);
        return user;
    }
}
