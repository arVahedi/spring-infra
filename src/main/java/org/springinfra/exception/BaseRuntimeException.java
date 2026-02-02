package org.springinfra.exception;

public abstract class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(Throwable ex) {
        super(ex);
    }

    public BaseRuntimeException(String message, Throwable ex) {
        super(message, ex);
    }
}
