/*pseudo code: Repository interface for managing `Author` database interactions
- import author entity
- import Jpa for built in crud methods for DB operations
- AutoRepository interface extendding Jpa (Jpa takes 2 type paramater: entity class + primary key type)
* */

package com.example.authorsystem.repository;

import com.example.authorsystem.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}

