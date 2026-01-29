package org.springinfra.exception;

public abstract class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(Exception ex) {
        super(ex);
    }

    public BaseRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}
