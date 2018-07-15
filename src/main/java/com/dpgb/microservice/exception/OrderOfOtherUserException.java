package com.dpgb.microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.FORBIDDEN)
public class OrderOfOtherUserException extends NotFoundException {
    public OrderOfOtherUserException(String message) {
        super(message);
    }
}

