package examples.model.dto.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenAuthenticationView {
    private String token;
}
