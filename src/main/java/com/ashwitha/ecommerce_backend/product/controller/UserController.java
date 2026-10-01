
package com.ashwitha.ecommerce_backend.product.controller;

import com.ashwitha.ecommerce_backend.config.JwtService;
import com.ashwitha.ecommerce_backend.product.model.LoginRequest;
import com.ashwitha.ecommerce_backend.product.model.User;
import com.ashwitha.ecommerce_backend.product.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user) {

        userService.registerUser(
                user.getFullName(),
                user.getEmail(),
                user.getPassword()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "User registered successfully"
                ));
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        boolean valid = userService.validateLogin(
                email,
                password
        );

        if (valid) {

            // Retrieve user details from the database
            User user = userService.getUserByEmail(email);

            // Generate JWT containing email and role
            String token = jwtService.generateToken(
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "token", token,
                            "email", user.getEmail(),
                            "role", user.getRole()
                    )
            );
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "message", "Invalid email or password"
                ));
    }
}