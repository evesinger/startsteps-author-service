/*pseudo code: Business logic, part of service layer, bridge between Controller and Repo
- importing necesseary dependencies (Author Entity/Repository, service, java util)
- Service layer: interecting with AuthorRepo to fetch CRUD, marks class as service component
+ The AuthorRepository instance is to be injected through the constructor.
+ Business logic methods
* */

package com.example.authorsystem.service;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.repository.AuthorRepository; // repo injection
import org.springframework.stereotype.Service;
import java.util.List; //List interface from Java utiliy to return List
import java.util.Optional;


@Service //class marked as Service component
    public class AuthorService {

        private final AuthorRepository authorRepository; // acess + mutability + type + variable(instance)
                                                        // declared variable not yet assigned value

        public AuthorService(AuthorRepository authorRepository) { // Constructor Injection
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

    // Create a new author
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Update an existing author
    public Author updateAuthor(Long id, Author authorDetails) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setFirstName(authorDetails.getFirstName());
                    author.setLastName(authorDetails.getLastName());
                    author.setEmail(authorDetails.getEmail());
                    author.setProfileImage(authorDetails.getProfileImage());
                    return authorRepository.save(author);
                })
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));
    }

    // Delete an author by ID
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with ID: " + id);
        }
        authorRepository.deleteById(id);
    }
}