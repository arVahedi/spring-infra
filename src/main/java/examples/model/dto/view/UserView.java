package examples.model.dto.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import examples.assets.UserStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UserView {
    @JsonProperty("id")
    private UUID publicId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private UserStatus status;
}
