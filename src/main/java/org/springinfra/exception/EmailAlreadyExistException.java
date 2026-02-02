package org.springinfra.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailAlreadyExistException extends UniqueConstraintAlreadyExistsException {

    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
