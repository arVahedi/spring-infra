package personal.project.springinfra.assets;


/**
 * Created by gladiator on 9/10/16.
 */
public enum ErrorCode {
    //region Errors
    NO_ERROR(200, "NO_ERROR", "user_message_no_error"),    // No error
    GENERAL_ERROR(6000, "GENERAL_ERROR", "user_message_general_error"),   // General Error
    EXPIRED_DATA(6001, "EXPIRED_DATA", "user_message_expired_data"),      // Expired data
    USER_LOCKED(6002, "USER_LOCKED", "user_message_user_locked"),   //USER LOCKED
    USER_SUSPENDED(6003, "USER_SUSPENDED", "user_message_user_suspended"),     //User is suspended
    USER_NOT_EXIST(6004, "USER_NOT_EXIST", "user_message_user_not_exist");
    //endregion

    //region Fields
    private int code;
    private String description;
    private String i18nKey;
    //endregion

    //region Constructors
    ErrorCode(int code, String description, String i18nKey) {
        this.code = code;
        this.description = description;
        this.i18nKey = i18nKey;
    }
    //endregion

    //region Getter and Setter
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getI18nKey() {
        return i18nKey;
    }
    //endregion
}