package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.dto.LoginRequest;
import com.example.ecommerce_backend.dto.RegisterRequest;
import com.example.ecommerce_backend.entity.User;
import com.example.ecommerce_backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- REGISTRATION ENDPOINT ---
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    // --- LOGIN ENDPOINT ---
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest request) {
        
        // 1. Find the user by email
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        
        // 2. Check if user exists
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("Error: User not found!", HttpStatus.NOT_FOUND);
        }
        
        User user = optionalUser.get();

        // 3. Compare the raw password with the hashed password in the database
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Error: Incorrect password!", HttpStatus.UNAUTHORIZED);
        }

        // 4. Success! (Later, we will return a JWT token here)
        return new ResponseEntity<>("Login successful! Welcome back, " + user.getName(), HttpStatus.OK);
    }
}