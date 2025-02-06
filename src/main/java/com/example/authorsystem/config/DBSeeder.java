package com.example.authorsystem.config;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DBSeeder {

    @Bean
    CommandLineRunner seedAuthors(AuthorRepository authorRepository) {
        return args -> {
            if (authorRepository.count() == 0) { // Only seed if table is empty
                String simplePassword = "kaching12345!";

                List<Author> authors = List.of(
                        new Author("Jane", "Doe", "jane.doe@example.com", simplePassword, "https://randomuser.me/api/portraits/women/1.jpg", "Bio of Jane", true),
                        new Author("John", "Smith", "john.smith@example.com", simplePassword, "https://randomuser.me/api/portraits/men/1.jpg", "Bio of John", true),
                        new Author("Emily", "Brown", "emily.brown@example.com", simplePassword, "https://randomuser.me/api/portraits/women/2.jpg", "Bio of Emily", true),
                        new Author("Michael", "Johnson", "michael.johnson@example.com", simplePassword, "https://randomuser.me/api/portraits/men/2.jpg", "Bio of Michael", true),
                        new Author("Sophia", "Taylor", "sophia.taylor@example.com", simplePassword, "https://randomuser.me/api/portraits/women/3.jpg", "Bio of Sophia", true)
                );

                authorRepository.saveAll(authors);
                System.out.println(" Seeded authors table successfully.");
            }
        };
    }
}