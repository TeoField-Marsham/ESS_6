package org.example.ess_6.repository;

import org.example.ess_6.model.Author;
import org.example.ess_6.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("select a from Author a " +
            "where lower(a.name) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Author> search(@Param("searchTerm") String searchTerm);
}
