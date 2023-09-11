package springinfra.assets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * A template for all responses that are return from APIs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTemplate<T> {
    private int statusCode;
    private T result;

    public ResponseTemplate(int statusCode) {
        this.statusCode = statusCode;
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

    public String makeResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
