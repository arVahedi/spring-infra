package springinfra.exception;

public class CryptographyException extends BaseRuntimeException {

    public CryptographyException() {
        super();
    }

    public CryptographyException(String message) {
        super(message);
    }

    public CryptographyException(Exception ex) {
        super(ex);
    }

    public CryptographyException(String message, Exception ex) {
        super(message, ex);
    }
}
