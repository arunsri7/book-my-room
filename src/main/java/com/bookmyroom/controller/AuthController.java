package com.bookmyroom.controller;

import com.bookmyroom.model.User;
import com.bookmyroom.model.UserRole;
import com.bookmyroom.security.CustomUserDetails;
import com.bookmyroom.security.JwtService;
import com.bookmyroom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Received registration request for email: {}", request.email());
        
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setCompanyId(request.companyId());
        user.setRole(UserRole.USER); // Default role is USER for now. We need to get inside the db to change the roles. TODO: have a user controller for this
        
        User savedUser = userService.createUser(user);
        log.info("Successfully registered user with email: {}", request.email());

        UserDetails userDetails = new CustomUserDetails(savedUser);

        String token = jwtService.generateToken(userDetails);
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", savedUser.getId());
        response.put("companyId", savedUser.getCompanyId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.email());
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", userDetails.getUser().getId());
        response.put("companyId", userDetails.getUser().getCompanyId());
        log.info("Successfully authenticated user with email: {}", request.email());
        return ResponseEntity.ok(response);
    }

    public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String companyId
    ) {}

    public record LoginRequest(
        String email,
        String password
    ) {}
} 