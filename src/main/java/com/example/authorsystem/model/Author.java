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

@Entity //table in DB
@Table(name="authors")// table name
@Getter //getter methods
@Setter // setter methods
@NoArgsConstructor // nor arg


public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment
    private Long id; // unique ids

    @Column(nullable = false) //can't be empty
    private String firstName;

    @Column(nullable = false)//can't be empty
    private String lastName;

    @Column(nullable = false, unique = true)//can't be empty, must be unique
    private String email;

    private String profileImage;

}
