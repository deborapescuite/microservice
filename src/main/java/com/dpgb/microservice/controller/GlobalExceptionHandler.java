package com.dpgb.microservice.controller;

import com.dpgb.microservice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        ErrorDetail exceptionResponse = new ErrorDetail(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(Exception ex) {
        ErrorDetail errorDetail = new ErrorDetail(ex.getMessage(), HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
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