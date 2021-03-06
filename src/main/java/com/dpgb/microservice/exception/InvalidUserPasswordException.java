package com.dpgb.microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidUserPasswordException extends RuntimeException {
    public InvalidUserPasswordException(String message) {
        super(message);
    }
}
