package springinfra.exception;

public class ObjectMappingException extends RuntimeException {
    public ObjectMappingException() {
    }

    public ObjectMappingException(Throwable ex) {
        super(ex);
    }

    public ObjectMappingException(String message, Throwable ex) {
        super(message, ex);
    }

    public ObjectMappingException(final String message) {
        super(message);
    }
}
