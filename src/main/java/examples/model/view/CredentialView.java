package examples.model.view;

import examples.model.dto.CreateUserRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CredentialView {
    private String username;
    private String password;
    private UserView user;
    private List<RoleView> roles = new ArrayList<>();
}
