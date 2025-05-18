package com.bookmyroom.exception;

import org.springframework.http.HttpStatus;

public class BookingConflictException extends BaseException {
    public BookingConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
} 