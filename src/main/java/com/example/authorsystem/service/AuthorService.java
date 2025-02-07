package com.example.authorsystem.service;

import com.example.authorsystem.model.Author;
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
    public Author updateAuthor(Long id, Author authorDetails) {
        return authorRepository.findById(id).map(author -> {
            author.setFirstName(authorDetails.getFirstName());
            author.setLastName(authorDetails.getLastName());
            author.setEmail(authorDetails.getEmail());
            author.setProfileImage(authorDetails.getProfileImage());
            author.setIntroduction(authorDetails.getIntroduction()); //
            return authorRepository.save(author);
        }).orElseThrow(() -> new RuntimeException("Author not found"));
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
}
