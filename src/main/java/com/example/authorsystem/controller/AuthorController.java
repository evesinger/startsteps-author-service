package com.example.authorsystem.controller;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    // Constructor-based dependency injection
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Get all authors
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    // Get author by ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> author = authorService.getAuthorById(id);
        return author.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an existing author
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        try {
            Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
            return ResponseEntity.ok(updatedAuthor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update only the introduction
    @PutMapping("/{id}/update-introduction")
    public ResponseEntity<?> updateIntroduction(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String newIntroduction = requestBody.get("introduction");

        if (newIntroduction == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Introduction cannot be null"));
        }

        try {
            Author updatedAuthor = authorService.updateIntroduction(id, newIntroduction);
            return ResponseEntity.ok(Map.of("message", "Introduction updated successfully", "author", updatedAuthor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an author by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.ok("Author deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
