package personal.project.springinfra.assets;

import lombok.experimental.UtilityClass;

public enum AuthorityType {
    USER_MANAGEMENT_AUTHORITY(String.USER_MANAGEMENT_AUTHORITY),
    ACCOUNT_INFO_AUTHORITY(String.ACCOUNT_INFO_AUTHORITY);

    private java.lang.String value;

    AuthorityType(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getValue() {
        return this.value;
    }

    public java.lang.String toString() {
        return this.value;
    }

    @UtilityClass
    public static class String {
        public final java.lang.String USER_MANAGEMENT_AUTHORITY = "USER_MANAGEMENT_AUTHORITY";
        public final java.lang.String ACCOUNT_INFO_AUTHORITY = "ACCOUNT_INFO_AUTHORITY";
    }
}
