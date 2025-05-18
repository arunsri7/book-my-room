package com.bookmyroom.service;

import com.bookmyroom.model.User;

public interface UserService {
    User createUser(User user);
    User findByEmail(String email);
    User findById(String id);
} 