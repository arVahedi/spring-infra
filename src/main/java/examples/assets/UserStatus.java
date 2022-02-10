package examples.assets;

import io.swagger.v3.oas.annotations.media.Schema;
import springinfra.assets.PersistableEnum;
import springinfra.utility.jpa.PersistableEnumConverter;

@Schema(enumAsRef = true)
public enum UserStatus implements PersistableEnum<UserStatus> {
    ACTIVE(1),
    INACTIVE(2),
    SUSPEND(3);

    private Integer code;

    UserStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public static class Converter extends PersistableEnumConverter<UserStatus> {
    }
}
