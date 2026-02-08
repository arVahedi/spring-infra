package examples.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springinfra.model.dto.request.BaseRequest;

public record AuthenticationRequest(
        @NotBlank(message = "username parameter is required and can not be blank")
        String username,
        @NotBlank(message = "username parameter is required and can not be blank")
        String password
) implements BaseRequest {

}
