package personal.project.springinfra.exception;

public class UserNotExistException extends BaseRuntimeException {

    public UserNotExistException() {
        super();
    }

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(Exception ex) {
        super(ex);
    }

    public UserNotExistException(String message, Exception ex) {
        super(message, ex);
    }
}
