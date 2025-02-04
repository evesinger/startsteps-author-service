/*
pseudo code: (Author Table in PostgreSQL)
-import API, annotations, getters/setter(lombok)
(Authors class represent authors table in the database, is mapped as a Entity for SB)
- Entity (make class DB entity)
- Table name
- Getter/Setter
- noArgument constructor
 */

package com.example.authorsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors") // Table name
@Getter
@Setter
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id; // Unique ID

    @Column(nullable = false) // Cannot be empty
    private String firstName;

    @Column(nullable = false) // Cannot be empty
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String profileImage;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE") // Auto-activate accounts
    private boolean active = true;

}



