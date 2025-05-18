package com.bookmyroom.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends BaseException {
    private final String entityType;
    private final String identifier;

    public DuplicateEntityException(String entityType, String identifier) {
        super(String.format("%s with %s already exists", entityType, identifier), HttpStatus.CONFLICT);
        this.entityType = entityType;
        this.identifier = identifier;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getIdentifier() {
        return identifier;
    }
} 