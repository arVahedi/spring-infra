package springinfra.exception;


public class UsernameAlreadyExistsException extends BaseRuntimeException {

    public UsernameAlreadyExistsException() {
        super();
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(Exception ex) {
        super(ex);
    }

    public UsernameAlreadyExistsException(String message, Exception ex) {
        super(message, ex);
    }
}
