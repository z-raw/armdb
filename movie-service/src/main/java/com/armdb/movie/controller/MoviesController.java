package com.armdb.movie.controller;

import com.armdb.movie.dto.MovieDTO;
import com.armdb.movie.entity.Title;
import com.armdb.movie.repository.TitleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    private final TitleRepository titleRepository;

    public MoviesController(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @GetMapping
    public ResponseEntity<?> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") boolean extended) {

        if (page_size > 1000) page_size = 1000;
        Pageable pageable = PageRequest.of(page, page_size, Sort.by("primaryTitle").ascending());
        
        Page<Title> titles;
        if (name != null && !name.isEmpty()) {
            titles = titleRepository.findByPrimaryTitleContainingIgnoreCase(name, pageable);
        } else {
            titles = titleRepository.findAll(pageable);
        }

        Page<MovieDTO> dtos = titles.map(t -> new MovieDTO(t.getId(), t.getPrimaryTitle(), t.getStartYear()));

        if (extended) {
            return ResponseEntity.ok(dtos);
        } else {
            return ResponseEntity.ok(dtos.getContent());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable java.util.UUID id) {
        return titleRepository.findById(id)
                .map(t -> ResponseEntity.ok(new MovieDTO(t.getId(), t.getPrimaryTitle(), t.getStartYear())))
                .orElse(ResponseEntity.notFound().build());
    }
}
