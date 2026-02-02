package examples.model.view;

import lombok.Data;
import org.springinfra.assets.AuthorityType;

import java.util.List;
import java.util.UUID;

@Data
public class RoleView {
    private UUID publicId;
    private String name;
    private List<AuthorityType> authorities;
}
