package org.springinfra.assets;


import lombok.Getter;

/**
 * An enum container for all business errors
 */

@Getter
public enum ErrorCode {
    EXPIRED_DATA(6001, "EXPIRED_DATA", "user_message_expired_data"),      // Expired data
    USER_LOCKED(6002, "USER_LOCKED", "user_message_user_locked"),   //USER LOCKED
    USER_SUSPENDED(6003, "USER_SUSPENDED", "user_message_user_suspended"),     //User is suspended
    USER_NOT_EXIST(6004, "USER_NOT_EXIST", "user_message_user_not_exist");     //User doesn't exist

    private final int code;
    private final String description;
    private final String i18nKey;

    ErrorCode(int code, String description, String i18nKey) {
        this.code = code;
        this.description = description;
        this.i18nKey = i18nKey;
    }
}