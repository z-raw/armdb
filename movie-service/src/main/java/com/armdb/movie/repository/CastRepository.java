package com.armdb.movie.repository;

import com.armdb.movie.entity.Cast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastRepository extends JpaRepository<Cast, Integer> {

    Page<Cast> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
