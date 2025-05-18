package com.bookmyroom.exception;

import org.springframework.http.HttpStatus;

public class DuplicateUserException extends BaseException {
    public DuplicateUserException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
} 