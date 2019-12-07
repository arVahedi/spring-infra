package personal.project.springinfra.exception.advisor;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import personal.project.springinfra.assets.ResponseTemplate;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionAdvisor extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            messages.add(fieldError.getDefaultMessage());
        });
        ResponseTemplate<List> responseTemplate = new ResponseTemplate(HttpStatus.BAD_REQUEST, messages);
        return new ResponseEntity(responseTemplate, HttpStatus.BAD_REQUEST);
    }
}
