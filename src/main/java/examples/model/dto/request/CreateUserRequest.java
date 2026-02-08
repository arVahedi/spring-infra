package examples.model.dto.request;

import examples.assets.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springinfra.assets.Regex;
import org.springinfra.model.dto.request.BaseRequest;


public record CreateUserRequest(
        @NotBlank(message = "First-Name is required")
        String firstName,
        @NotBlank(message = "Last-Name is required")
        String lastName,
        @Pattern(regexp = Regex.EMAIL, message = "Email format is wrong")
        String email,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = Regex.PHONE_NUMBER, message = "Phone number format is wrong")
        String phone,
        @NotNull(message = "status is required")
        UserStatus status
) implements BaseRequest {

}
