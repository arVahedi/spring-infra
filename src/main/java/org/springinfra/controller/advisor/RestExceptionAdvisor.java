package org.springinfra.controller.advisor;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springinfra.assets.Constant;
import org.springinfra.assets.ErrorCode;
import org.springinfra.assets.ResponseTemplate;
import org.springinfra.exception.EmailAlreadyExistException;
import org.springinfra.exception.UsernameAlreadyExistsException;
import org.springinfra.system.listener.SuccessfulAuthenticationHandler;
import org.springinfra.utility.http.HttpRequestUtil;
import org.springinfra.utility.identity.IdentityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class RestExceptionAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({StaleObjectStateException.class, ObjectOptimisticLockingFailureException.class, EntityNotFoundException.class})
    public ResponseEntity<ResponseTemplate<String>> handleExpiredDataException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ResponseTemplate<String> responseTemplate = new ResponseTemplate<>(ErrorCode.EXPIRED_DATA, "Row was updated or deleted by another transaction");
        return new ResponseEntity<>(responseTemplate, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseTemplate<List<String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        List<String> messages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        ResponseTemplate<List<String>> responseTemplate = new ResponseTemplate<>(HttpStatus.BAD_REQUEST, messages);
        return new ResponseEntity<>(responseTemplate, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseTemplate<String>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (HttpRequestUtil.isUiRequest(request)) {
                String redirectUrl = IdentityUtil.isAuthenticated() ? "/403" : "/login";

                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", redirectUrl);
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        HttpStatus status = IdentityUtil.isAuthenticated() ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).build();
    }

    @ExceptionHandler({BadCredentialsException.class, InvalidBearerTokenException.class, JwtValidationException.class, AuthenticationServiceException.class})
    public ResponseEntity<ResponseTemplate<String>> handleBadCredentialsException(Exception ex) {
        log.error(ex.getMessage(), ex);
        try {
            ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                if (HttpRequestUtil.isUiRequest(request)) {
                    HttpServletResponse httpServletResponse = requestAttributes.getResponse();
                    if (httpServletResponse != null && Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equalsIgnoreCase(Constant.AUTHORIZATION_TOKEN_COOKIE_NAME))) {
                        httpServletResponse.addCookie(SuccessfulAuthenticationHandler.generateAuthorizationCookie(Optional.empty()));
                    }

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", "/login");
                    return new ResponseEntity<>(headers, HttpStatus.FOUND);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ResponseTemplate<String>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseTemplate<>(HttpStatus.CONFLICT, "Username already exists"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ResponseTemplate<String>> handleUsernameAlreadyExistsException(EmailAlreadyExistException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseTemplate<>(HttpStatus.CONFLICT, "Email already exists"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ResponseTemplate<String>> handleGeneralException(PropertyReferenceException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseTemplate<>(HttpStatus.BAD_REQUEST, "No property found"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ResponseTemplate<String>> handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));
        ResponseTemplate<List<String>> responseTemplate = new ResponseTemplate<>(HttpStatus.BAD_REQUEST, messages);
        return new ResponseEntity<>(responseTemplate, HttpStatus.BAD_REQUEST);
    }
}
