package examples.model.dto.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springinfra.assets.AuthorityType;

import java.util.List;
import java.util.UUID;

@Data
public class RoleView {
    @JsonProperty("id")
    private UUID publicId;
    private String name;
    private List<AuthorityType> authorities;
}
