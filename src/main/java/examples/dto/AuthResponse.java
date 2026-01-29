package examples.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springinfra.model.dto.BaseDto;

@Getter
@Setter
@Builder
public class AuthResponse extends BaseDto {
    private String token;
}
