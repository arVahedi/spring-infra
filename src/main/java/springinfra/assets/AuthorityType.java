package springinfra.assets;


import lombok.Getter;

@Getter
public enum AuthorityType {
    MONITORING_AUTHORITY(StringFormat.MONITORING_AUTHORITY),
    USER_MANAGEMENT_AUTHORITY(StringFormat.USER_MANAGEMENT_AUTHORITY),
    ACCOUNT_INFO_AUTHORITY(StringFormat.ACCOUNT_INFO_AUTHORITY);

    private final String value;

    AuthorityType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static class StringFormat {
        public static final String MONITORING_AUTHORITY = "MONITORING_AUTHORITY";
        public static final String USER_MANAGEMENT_AUTHORITY = "USER_MANAGEMENT_AUTHORITY";
        public static final String ACCOUNT_INFO_AUTHORITY = "ACCOUNT_INFO_AUTHORITY";

        private StringFormat() {
            throw new AssertionError("This is a utility class and cannot be instantiated");
        }
    }
}
