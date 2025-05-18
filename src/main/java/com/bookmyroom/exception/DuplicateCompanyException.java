package com.bookmyroom.exception;

import org.springframework.http.HttpStatus;

public class DuplicateCompanyException extends BaseException {
    public DuplicateCompanyException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
} 