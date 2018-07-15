package com.dpgb.microservice.controller;

import com.dpgb.microservice.exception.InvalidUsePasswordException;
import com.dpgb.microservice.exception.NotFoundException;
import com.dpgb.microservice.exception.UserAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(Exception ex) {
        ErrorDetail errorDetail = new ErrorDetail(ex.getMessage(), HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorDetail> errors = new ArrayList<ErrorDetail>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorDetail(error.getField() + ": " + error.getDefaultMessage(), HttpStatus.BAD_REQUEST.toString()));
        }
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidUsePasswordException.class, UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleInvalidUserPassword(Exception ex) {
        ErrorDetail errorDetail = new ErrorDetail(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.toString());
        return new ResponseEntity<>(errorDetail, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    class ErrorDetail {
        private LocalDateTime timestamp;
        private String message;
        private String statusCode;

        public ErrorDetail(String message, String statusCode) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
            this.statusCode = statusCode;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }
    }
}