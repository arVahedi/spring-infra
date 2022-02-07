package springinfra.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
}
