package com.armdb.movie.repository;

import com.armdb.movie.entity.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, java.util.UUID> {
    
    Page<Title> findByPrimaryTitleContainingIgnoreCase(String primaryTitle, Pageable pageable);
}
