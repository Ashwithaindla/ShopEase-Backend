
package com.ashwitha.ecommerce_backend.product.service;

import com.ashwitha.ecommerce_backend.product.model.User;
import com.ashwitha.ecommerce_backend.product.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user
    public User registerUser(
            String fullName,
            String email,
            String password) {

        // Check whether email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);

        // Hash the password before saving
        user.setPassword(
                passwordEncoder.encode(password)
        );

        // Assign the default role to new users

        user.setRole("ROLE_USER");

        // Save the user in the database
        return userRepository.save(user);
    }

    // Validate user login
    public boolean validateLogin(
            String email,
            String password) {

        return userRepository.findByEmail(email)
                .map(user ->
                        passwordEncoder.matches(
                                password,
                                user.getPassword()
                        )
                )
                .orElse(false);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

}