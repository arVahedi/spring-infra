package springinfra.assets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

/**
 * Created by gladiator on 2/24/16.
 */
public class ResponseTemplate<T> {
    //region Fields
    private int statusCode;
    private T result;
    //endregion

    //region Constructors
    public ResponseTemplate() {
    }

    public ResponseTemplate(int statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseTemplate(int statusCode, T result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public ResponseTemplate(HttpStatus httpStatus) {
        this.statusCode = httpStatus.value();
        this.result = (T) httpStatus.getReasonPhrase();
    }

    public ResponseTemplate(HttpStatus httpStatus, T result) {
        this.statusCode = httpStatus.value();
        this.result = result;
    }

    public ResponseTemplate(ErrorCode error) {
        this.statusCode = error.getCode();
        this.result = (T) error.getDescription();
    }

    public ResponseTemplate(ErrorCode error, T result) {
        this.statusCode = error.getCode();
        this.result = result;
    }
    //endregion

    public String makeResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    //region Getter and Setter
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
    //endregion
}
