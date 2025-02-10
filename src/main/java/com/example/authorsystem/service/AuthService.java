package com.example.authorsystem.service;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.model.Role;
import com.example.authorsystem.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthorRepository authorRepository;

    public AuthService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Register an author, default role AUTHOR (Chiefs can only be promoted)
    public Author registerAuthor(Author author) {
        if (author.getPassword() == null || author.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            author.setFirstName("Unknown");
        }
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            author.setLastName("Unknown");
        }

        // Default role is author
        author.setRole(Role.AUTHOR);
        author.setActive(true);

        return authorRepository.save(author);
    }

    // Authenticate author, return author_id and role
    public Optional<Map<String, Object>> authenticate(String email, String password) {
        Optional<Author> author = authorRepository.findByEmail(email);

        if (author.isPresent()) {
            String storedPassword = author.get().getPassword();

            // Directly compare plain text passwords
            if (password.equals(storedPassword)) {
                System.out.println("Authentication successful for email: " + email);

                // Return response with author_id and role
                Map<String, Object> response = new HashMap<>();
                response.put("author_id", author.get().getId());
                response.put("first_name", author.get().getFirstName());
                response.put("last_name", author.get().getLastName());
                response.put("email", author.get().getEmail());
                response.put("role", author.get().getRole().name()); // Convert Enum to String

                System.out.println("Login Response: " + response);
                return Optional.of(response);
            } else {
                System.out.println("Password mismatch for email: " + email);
            }
        } else {
            System.out.println("Email not found: " + email);
        }

        return Optional.empty();
    }

    // Update an author's role (Only Chiefs)
    public Author updateAuthorRole(Long authorId, Long targetAuthorId, Role newRole) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        // Forbid if not chief
        if (author.getRole() != Role.CHIEF_EDITOR) {
            throw new RuntimeException("Only Chief Editors can update roles.");
        }

        // Find the author to update
        Author targetAuthor = authorRepository.findById(targetAuthorId)
                .orElseThrow(() -> new RuntimeException("Target Author not found"));

        // Update and save
        targetAuthor.setRole(newRole);
        return authorRepository.save(targetAuthor);
    }
}
