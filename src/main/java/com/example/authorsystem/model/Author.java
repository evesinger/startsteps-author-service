package com.example.authorsystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long id;

    @JsonProperty("first_name") // Maps JSON "first_name" to Java "firstName"
    @Column(nullable = false)
    private String firstName;

    @JsonProperty("last_name") // Maps JSON "last_name" to Java "lastName"
    @Column(nullable = false)
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
    private Role role;

    //Constructor (excluding ID because it's auto-generated)
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