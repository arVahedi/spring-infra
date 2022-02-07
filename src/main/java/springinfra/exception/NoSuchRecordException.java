package springinfra.exception;

public class NoSuchRecordException extends BaseRuntimeException {

    public NoSuchRecordException() {
        super();
    }

    public NoSuchRecordException(String message) {
        super(message);
    }

    public NoSuchRecordException(Exception ex) {
        super(ex);
    }

    public NoSuchRecordException(String message, Exception ex) {
        super(message, ex);
    }
}
