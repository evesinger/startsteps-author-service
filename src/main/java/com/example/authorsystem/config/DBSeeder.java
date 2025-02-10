package com.example.authorsystem.config;

import com.example.authorsystem.model.Author;
import com.example.authorsystem.repository.AuthorRepository;
import com.example.authorsystem.model.Role; // Import the Role enum
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class DBSeeder {

    @Bean
    CommandLineRunner seedDatabase(AuthorRepository authorRepository, JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("Resetting and seeding the database...");

            // 1Clear activity log first
            jdbcTemplate.execute("DELETE FROM activity_log;");
            System.out.println("🗑 Cleared existing activity logs.");

            // Delete all authors (to resets ids)
            authorRepository.deleteAll();
            jdbcTemplate.execute("ALTER SEQUENCE authors_id_seq RESTART WITH 1;"); // PostgreSQL-specific
            System.out.println("🗑 Cleared existing authors and reset ID sequence.");

            // 3Insert authors
            List<Author> authors = List.of(
                    new Author("Jane", "Doe", "jane.doe@example.com", "kaching12345!",
                            "https://randomuser.me/api/portraits/women/1.jpg", "Bio of Jane", true, Role.AUTHOR),
                    new Author("John", "Smith", "john.smith@example.com", "kaching12345!",
                            "https://randomuser.me/api/portraits/men/1.jpg", "Bio of John", true, Role.AUTHOR),
                    new Author("Emily", "Brown", "emily.brown@example.com", "kaching12345!",
                            "https://randomuser.me/api/portraits/women/2.jpg", "Bio of Emily", true, Role.AUTHOR),
                    new Author("Michael", "Johnson", "michael.johnson@example.com", "kaching12345!",
                            "https://randomuser.me/api/portraits/men/2.jpg", "Bio of Michael", true, Role.AUTHOR),
                    new Author("Liza", "Taylor", "sophia.taylor@example.com", "kaching12345!",
                            "https://randomuser.me/api/portraits/women/3.jpg", "Bio of Sophia", true, Role.CHIEF_EDITOR)
            );

            List<Author> savedAuthors = authorRepository.saveAll(authors);
            System.out.println("✅ Successfully reseeded authors table.");

            // Activity log is updated with the correct author id
            for (Author author : savedAuthors) {
                jdbcTemplate.update("UPDATE activity_log SET author_id = ? WHERE author_id IS NULL;", author.getId());
            }
            System.out.println("✅ Updated activity log with correct author IDs.");
        };
    }
}

