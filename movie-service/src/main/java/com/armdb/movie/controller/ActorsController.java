package com.armdb.movie.controller;

import com.armdb.movie.dto.ActorDTO;
import com.armdb.movie.dto.AppearanceDTO;
import com.armdb.movie.entity.Cast;
import com.armdb.movie.entity.TitleCast;
import com.armdb.movie.repository.CastRepository;
import com.armdb.movie.repository.TitleCastRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actors")
public class ActorsController {

    private final CastRepository castRepository;
    private final TitleCastRepository titleCastRepository;

    public ActorsController(CastRepository castRepository, TitleCastRepository titleCastRepository) {
        this.castRepository = castRepository;
        this.titleCastRepository = titleCastRepository;
    }

    @GetMapping
    public ResponseEntity<?> getActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") boolean extended) {

        if (page_size > 1000) page_size = 1000;
        Pageable pageable = PageRequest.of(page, page_size);

        Page<Cast> casts;
        if (name != null && !name.isEmpty()) {
            name = name.replaceAll(" ", "&");
            casts = castRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            casts = castRepository.findAll(pageable);
        }

        Page<ActorDTO> dtos = casts.map(c -> new ActorDTO(c.getId(), c.getName(), c.getBirthYear(), c.getDeathYear(), c.getPrimaryProfession(), c.getKnownForTitles(), c.getAge(), c.getIsAlive()));

        if (extended) {
            return ResponseEntity.ok(dtos);
        } else {
            return ResponseEntity.ok(dtos.getContent());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActor(@PathVariable java.util.UUID id) {
        return castRepository.findById(id)
                .map(c -> ResponseEntity.ok(new ActorDTO(c.getId(), c.getName(), c.getBirthYear(), c.getDeathYear(), c.getPrimaryProfession(), c.getKnownForTitles(), c.getAge(), c.getIsAlive())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/appearances")
    public ResponseEntity<?> getAppearances(
            @PathVariable java.util.UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(defaultValue = "false") boolean extended) {
        
        if (page_size > 1000) page_size = 1000;
        Pageable pageable = PageRequest.of(page, page_size, Sort.by("title.primaryTitle").ascending()); // Ordering by movie name

        Page<TitleCast> appearances = titleCastRepository.findByCastId(id, pageable);
        
        Page<AppearanceDTO> dtos = appearances.map(tc -> new AppearanceDTO(
                tc.getTitle().getId(),
                tc.getTitle().getPrimaryTitle(),
                tc.getCharacter(),
                tc.getTitle().getStartYear()
        ));

        if (extended) {
            return ResponseEntity.ok(dtos);
        } else {
            return ResponseEntity.ok(dtos.getContent());
        }
    }
}
