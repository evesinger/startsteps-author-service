package com.example.authorsystem.controller;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000") // Allowing FE requests (not working without this)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor-based dependency injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register a New Author and Return `author_id`
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Author author) {
        try {
            Author registeredAuthor = authService.registerAuthor(author);
            System.out.println("Registration successful for: " + registeredAuthor.getEmail());

            return ResponseEntity.ok(Map.of(
                    "author_id", registeredAuthor.getId(),
                    "email", registeredAuthor.getEmail()
            ));
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Login an Author and Return `author_id`
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        System.out.println("Login attempt with email: " + email);

        Optional<Map<String, Object>> authResult = authService.authenticate(email, password);

        if (authResult.isEmpty()) {
            System.out.println("Login failed for email: " + email);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials."));
        }

        // Debugging: Log the full response before sending it
        Map<String, Object> response = authResult.get();
        System.out.println("Sending response: " + response);

        return ResponseEntity.ok(response);
    }
}
