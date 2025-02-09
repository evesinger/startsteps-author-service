package com.example.authorsystem.service;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.model.Role;
import com.example.authorsystem.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    // Constructor-based dependency injection
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Get all authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Get author by ID
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    // Update an existing author
    public Author updateAuthor(Long id, Author updatedDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author with ID " + id + " not found."));

        if (updatedDetails.getFirstName() != null && !updatedDetails.getFirstName().isEmpty()) {
            author.setFirstName(updatedDetails.getFirstName());
        }
        if (updatedDetails.getLastName() != null && !updatedDetails.getLastName().isEmpty()) {
            author.setLastName(updatedDetails.getLastName());
        }
        if (updatedDetails.getEmail() != null && !updatedDetails.getEmail().isEmpty()) {
            author.setEmail(updatedDetails.getEmail());
        }
        if (updatedDetails.getProfileImage() != null) {
            author.setProfileImage(updatedDetails.getProfileImage());
        }
        if (updatedDetails.getIntroduction() != null) {
            author.setIntroduction(updatedDetails.getIntroduction());
        }
        if (updatedDetails.getRole() != null) {
            author.setRole(updatedDetails.getRole());
        }
        author.setActive(updatedDetails.isActive());

        // only update password if a new one is provided
        if (updatedDetails.getPassword() != null && !updatedDetails.getPassword().isEmpty()) {
            author.setPassword(updatedDetails.getPassword());
        }

        return authorRepository.save(author);
    }


    public Author updateIntroduction(Long id, String newIntroduction) {
        return authorRepository.findById(id).map(author -> {
            author.setIntroduction(newIntroduction);
            return authorRepository.save(author);
        }).orElseThrow(() -> new RuntimeException("Author not found"));
    }


    // Delete an author by ID
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found");
        }
        authorRepository.deleteById(id);
    }

    public Author updateAuthorRole(Long authorId, String newRole) {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        if (authorOpt.isEmpty()) {
            throw new RuntimeException("Author not found");
        }

        Author author = authorOpt.get();
        author.setRole(Role.valueOf(newRole)); // Convert string to Enum!!
        return authorRepository.save(author);
    }

}
