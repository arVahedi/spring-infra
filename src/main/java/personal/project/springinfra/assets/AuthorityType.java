package personal.project.springinfra.assets;

public class AuthorityType {
    public static final String USER_MANAGEMENT_AUTHORITY = "USER_MANAGEMENT_AUTHORITY";
    public static final String ACCOUNT_INFO_AUTHORITY = "ACCOUNT_INFO_AUTHORITY";

    private AuthorityType() {
        throw new AssertionError("This is a utility class and cannot be instantiated");
    }
}
