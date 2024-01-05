package org.example.ess_6.repository;

import org.example.ess_6.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("select p from Publisher p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Publisher> search(@Param("searchTerm") String searchTerm);
}
