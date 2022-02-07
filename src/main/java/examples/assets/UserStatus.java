package examples.assets;

import springinfra.assets.PersistableEnum;
import springinfra.utility.jpa.PersistableEnumConverter;

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
