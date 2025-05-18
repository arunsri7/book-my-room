package com.bookmyroom.model;

public enum UserRole {
    SUPER_ADMIN,
    COMPANY_ADMIN,
    USER;

    public String getRole() {
        return "ROLE_" + this.name();
    }
} 