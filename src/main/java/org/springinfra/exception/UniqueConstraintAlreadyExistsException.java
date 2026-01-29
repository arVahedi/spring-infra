package org.springinfra.exception;

public class UniqueConstraintAlreadyExistsException extends BaseRuntimeException {

    public UniqueConstraintAlreadyExistsException() {
        super();
    }

    public UniqueConstraintAlreadyExistsException(String message) {
        super(message);
    }

    public UniqueConstraintAlreadyExistsException(Exception ex) {
        super(ex);
    }

    public UniqueConstraintAlreadyExistsException(String message, Exception ex) {
        super(message, ex);
    }
}
