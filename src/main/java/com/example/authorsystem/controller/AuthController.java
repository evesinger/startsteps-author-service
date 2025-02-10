package com.example.authorsystem.controller;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.model.Role;
import com.example.authorsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register a new Author with default role `AUTHOR`
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Author author) {
        try {
            // Default all new users to AUTHOR role
            author.setRole(Role.AUTHOR);

            Author registeredAuthor = authService.registerAuthor(author);
            return ResponseEntity.ok(Map.of(
                    "author_id", registeredAuthor.getId(),
                    "first_name", registeredAuthor.getFirstName(),
                    "last_name", registeredAuthor.getLastName(),
                    "email", registeredAuthor.getEmail(),
                    "profile_image", registeredAuthor.getProfileImage(),
                    "role", registeredAuthor.getRole().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<Map<String, Object>> authResult = authService.authenticate(email, password);
        if (authResult.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials."));
        }

        return ResponseEntity.ok(authResult.get());
    }

    // Update role (only chiefs)
    @PutMapping("/update-role/{id}")
    public ResponseEntity<?> updateAuthorRole(
            @PathVariable Long id,
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole,
            @RequestBody Map<String, String> requestBody) {
        try {
            String newRoleString = requestBody.get("role");

            if (newRoleString == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Role is required."));
            }
            // Convert role to ENUM
            Role newRole = Role.valueOf(newRoleString.toUpperCase());

            // Only chiefs
            if (!userRole.equalsIgnoreCase("CHIEF_EDITOR")) {
                return ResponseEntity.status(403).body(Map.of("error", "Only Chief Editors can update roles."));
            }

            Author updatedAuthor = authService.updateAuthorRole(authorId, id, newRole);

            return ResponseEntity.ok(Map.of(
                    "message", "Role updated successfully",
                    "author_id", updatedAuthor.getId(),
                    "email", updatedAuthor.getEmail(),
                    "role", updatedAuthor.getRole().name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role. Allowed values: AUTHOR, CHIEF_EDITOR"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }
}
