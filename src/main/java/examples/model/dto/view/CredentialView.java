package examples.model.dto.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CredentialView {
    @JsonProperty("id")
    private UUID publicId;
    private String username;
    private String password;
    private UserView user;
    private List<RoleView> roles = new ArrayList<>();
}
