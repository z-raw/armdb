package com.armdb.movie.repository;

import com.armdb.movie.entity.Cast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CastRepository extends JpaRepository<Cast, java.util.UUID> {

    @Query(value = "SELECT * FROM casts WHERE to_tsvector('english', primary_name) @@ to_tsquery('english', ?1)",
        countQuery = "SELECT count(*) FROM casts WHERE to_tsvector('english', primary_name) @@ to_tsquery('english', ?1)",
        nativeQuery = true
    )
    Page<Cast> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
