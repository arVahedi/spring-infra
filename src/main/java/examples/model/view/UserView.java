package examples.model.view;

import examples.assets.UserStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UserView {
    private UUID publicId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private UserStatus status;
}
