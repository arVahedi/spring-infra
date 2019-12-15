package personal.project.springinfra.exception.advisor;


import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import personal.project.springinfra.assets.ErrorCode;
import personal.project.springinfra.assets.ResponseTemplate;
import personal.project.springinfra.exception.NoSuchRecordException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoSuchRecordException.class, StaleObjectStateException.class, ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<ResponseTemplate> handleExpiredDataException(Exception ex) {
        ResponseTemplate<String> responseTemplate = new ResponseTemplate<>(ErrorCode.EXPIRED_DATA, "Row was updated or deleted by another transaction");
        return new ResponseEntity<>(responseTemplate, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseTemplate> handleGeneralException(Exception ex) {
        ResponseTemplate<String> responseTemplate = new ResponseTemplate<>(ErrorCode.GENERAL_ERROR, ex.getMessage() != null ? ex.getMessage() : ErrorCode.GENERAL_ERROR.getDescription());
        return new ResponseEntity<>(responseTemplate, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));
        ResponseTemplate<List> responseTemplate = new ResponseTemplate<>(HttpStatus.BAD_REQUEST, messages);
        return new ResponseEntity<>(responseTemplate, HttpStatus.BAD_REQUEST);
    }
}
