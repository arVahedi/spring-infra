package org.springinfra.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameAlreadyExistsException extends UniqueConstraintAlreadyExistsException {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

}
