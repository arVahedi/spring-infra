package springinfra.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
}
