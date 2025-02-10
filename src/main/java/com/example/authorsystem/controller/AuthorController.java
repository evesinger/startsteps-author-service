package com.example.authorsystem.controller;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Get all authors (both roles)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllAuthors(
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole) {

        if (!userRole.equalsIgnoreCase("CHIEF_EDITOR") && !userRole.equalsIgnoreCase("AUTHOR")) {
            return ResponseEntity.status(403).body(List.of(Map.of("error", "Access Denied")));
        }

        List<Map<String, Object>> safeAuthors = authorService.getAllAuthors().stream().map(author -> {
            Map<String, Object> authorData = new HashMap<>();
            authorData.put("id", author.getId());
            authorData.put("firstName", author.getFirstName());
            authorData.put("lastName", author.getLastName());
            authorData.put("email", author.getEmail());
            authorData.put("profileImage", author.getProfileImage());
            authorData.put("introduction", author.getIntroduction());
            authorData.put("active", author.isActive());
            authorData.put("role", author.getRole().name());
            return authorData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(safeAuthors);
    }


    // Get an author by id (both roles due to profile page)
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(
            @PathVariable Long id,
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole) {

        System.out.println("🔍 Request to fetch author ID: " + id);
        System.out.println("🔍 Logged-in Author ID: " + authorId);
        System.out.println("🔍 User Role: " + userRole);

        // AUTHORS can only fetch their own profile (+404 error debugging)
        if (userRole.equalsIgnoreCase("AUTHOR") && !authorId.equals(id)) {
            System.out.println("Access denied: AUTHOR tried to fetch another author!");
            return ResponseEntity.status(403).body(Map.of("error", "Authors can only view their own profile."));
        }

        Optional<Author> authorOpt = authorService.getAuthorById(id);

        if (authorOpt.isEmpty()) {
            System.out.println("Author not found with ID: " + id);
            return ResponseEntity.status(404).body(Map.of("error", "Author not found."));
        }

        Author author = authorOpt.get();
        System.out.println("Found author: " + author.getFirstName() + " " + author.getLastName());

        Map<String, Object> authorData = new HashMap<>();
        authorData.put("id", author.getId());
        authorData.put("firstName", author.getFirstName());
        authorData.put("lastName", author.getLastName());
        authorData.put("email", author.getEmail());
        authorData.put("profileImage", author.getProfileImage());
        authorData.put("introduction", author.getIntroduction());
        authorData.put("role", author.getRole().name());

        return ResponseEntity.ok(authorData);
    }

    // Update an author (only chiefs)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(
            @PathVariable Long id,
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole,
            @RequestBody Author authorDetails) {
        if (!userRole.equalsIgnoreCase("CHIEF_EDITOR")) {
            return ResponseEntity.status(403).body(Map.of("error", "Only Chief Editors can update authors."));
        }

        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        if (updatedAuthor == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Author with ID " + id + " not found."));
        }

        Map<String, Object> updatedAuthorData = new HashMap<>();
        updatedAuthorData.put("id", updatedAuthor.getId());
        updatedAuthorData.put("firstName", updatedAuthor.getFirstName());
        updatedAuthorData.put("lastName", updatedAuthor.getLastName());
        updatedAuthorData.put("email", updatedAuthor.getEmail());
        updatedAuthorData.put("profileImage", updatedAuthor.getProfileImage());
        updatedAuthorData.put("introduction", updatedAuthor.getIntroduction());
        updatedAuthorData.put("active", updatedAuthor.isActive());
        updatedAuthorData.put("role", updatedAuthor.getRole().name());

        return ResponseEntity.ok(updatedAuthorData);
    }

    // Update interoduction (only Authors)
    @PutMapping("/{id}/update-introduction")
    public ResponseEntity<?> updateIntroduction(
            @PathVariable Long id,
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole,
            @RequestBody Map<String, String> requestBody) {
        String newIntroduction = requestBody.get("introduction");

        if (newIntroduction == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Introduction cannot be null"));
        }

        if (!authorId.equals(id)) {
            return ResponseEntity.status(403).body(Map.of("error", "Authors can only update their own introduction."));
        }

        try {
            Author updatedAuthor = authorService.updateIntroduction(id, newIntroduction);
            return ResponseEntity.ok(Map.of("message", "Introduction updated successfully", "author", updatedAuthor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Author not found"));
        }
    }

    // Change role permissions (only Chiefs)
    @PutMapping("/{id}/change-role")
    public ResponseEntity<?> changeAuthorRole(
            @PathVariable Long id,
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole,
            @RequestBody Map<String, String> requestBody) {

        if (!userRole.equalsIgnoreCase("CHIEF_EDITOR")) {
            return ResponseEntity.status(403).body(Map.of("error", "Only Chief Editors can change author roles."));
        }

        String newRole = requestBody.get("role");
        if (newRole == null || (!newRole.equalsIgnoreCase("AUTHOR") && !newRole.equalsIgnoreCase("CHIEF_EDITOR"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role. Must be AUTHOR or CHIEF_EDITOR."));
        }

        try {
            Author updatedAuthor = authorService.updateAuthorRole(id, newRole);
            return ResponseEntity.ok(Map.of("message", "Role updated successfully", "author", updatedAuthor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Author not found"));
        }
    }


    // Delete an Author (only Chief)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(
            @PathVariable Long id,
            @RequestHeader("x-author-id") Long authorId,
            @RequestHeader("x-user-role") String userRole) {
        if (!userRole.equalsIgnoreCase("CHIEF_EDITOR")) {
            return ResponseEntity.status(403).body(Map.of("error", "Only Chief Editors can delete authors."));
        }

        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.ok(Map.of("message", "Author deleted successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
