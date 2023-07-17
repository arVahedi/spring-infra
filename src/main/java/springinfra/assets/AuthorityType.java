package springinfra.assets;


public enum AuthorityType {
    USER_MANAGEMENT_AUTHORITY(StringFormat.USER_MANAGEMENT_AUTHORITY),
    ACCOUNT_INFO_AUTHORITY(StringFormat.ACCOUNT_INFO_AUTHORITY);

    private final String value;

    AuthorityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static class StringFormat {
        public static final String USER_MANAGEMENT_AUTHORITY = "USER_MANAGEMENT_AUTHORITY";
        public static final String ACCOUNT_INFO_AUTHORITY = "ACCOUNT_INFO_AUTHORITY";

        private StringFormat() {
            throw new AssertionError("This is a utility class and cannot be instantiated");
        }
    }
}
