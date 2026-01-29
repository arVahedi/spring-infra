package org.springinfra.exception;

public abstract class BaseException extends Exception {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Exception ex) {
        super(ex);
    }

    public BaseException(String message, Exception ex) {
        super(message, ex);
    }
}
