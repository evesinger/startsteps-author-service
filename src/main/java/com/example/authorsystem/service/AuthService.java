package com.example.authorsystem.service;

import com.example.authorsystem.model.Author;
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

    // Register auuthor
    public Author registerAuthor(Author author) {
        if (author.getPassword() == null || author.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // No hashing, store as plain text
        author.setActive(true);
        return authorRepository.save(author);
    }

    // Authenticate author using plain text password comparison
    public Optional<Map<String, Object>> authenticate(String email, String password) {
        Optional<Author> author = authorRepository.findByEmail(email);

        if (author.isPresent()) {
            String storedPassword = author.get().getPassword();

            // Directly compare plain text passwords
            if (password.equals(storedPassword)) {
                System.out.println("✅ Authentication successful for email: " + email);

                // Return response with `author_id` and `role`
                Map<String, Object> response = new HashMap<>();
                response.put("author_id", author.get().getId());
                response.put("email", author.get().getEmail());
                response.put("role", author.get().getRole().name()); // Convert Enum to String

                System.out.println("🔹 Login Response: " + response);
                return Optional.of(response);
            } else {
                System.out.println("❌ Password mismatch for email: " + email);
            }
        } else {
            System.out.println("❌ Email not found: " + email);
        }

        return Optional.empty();
    }
}
