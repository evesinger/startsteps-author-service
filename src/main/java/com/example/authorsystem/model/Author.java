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

    @Enumerated(EnumType.STRING) // Store as string in DB
    @Column(nullable = false)
    private Role role; // New role column

    //Correct constructor (excluding ID because it's auto-generated)
    public Author(String firstName, String lastName, String email, String password, String profileImage, String introduction, boolean active, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.active = active;
        this.role = role;
    }
}