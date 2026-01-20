package com.armdb.movie;

import com.armdb.movie.entity.Cast;
import com.armdb.movie.entity.Title;
import com.armdb.movie.entity.TitleCast;
import com.armdb.movie.entity.TitleCastId;
import com.armdb.movie.repository.CastRepository;
import com.armdb.movie.repository.TitleCastRepository;
import com.armdb.movie.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class MovieIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private CastRepository castRepository;

    @Autowired
    private TitleCastRepository titleCastRepository;

    private UUID movieId;
    private UUID actorId;

    @BeforeEach
    void setUp() {
        titleCastRepository.deleteAll();
        titleRepository.deleteAll();
        castRepository.deleteAll();

        Title movie = new Title();
        movie.setPrimaryTitle("Inception");
        movie.setStartYear(2010);
        movie.setTconst("tt1375666");
        movie.setGenres("Action,Sci-Fi");
        movie = titleRepository.save(movie);
        movieId = movie.getId();

        Cast actor = new Cast();
        actor.setName("Leonardo DiCaprio");
        actor.setBirthYear(1974);
        actor.setPrimaryProfession("Actor,Producer");
        actor.setNconst("nm0000138");
        actor = castRepository.save(actor);
        actorId = actor.getId();

        TitleCast appearance = new TitleCast();
        TitleCastId appearanceId = new TitleCastId();
        appearanceId.setTitleId(movieId);
        appearanceId.setCastId(actorId);
        appearance.setId(appearanceId);
        appearance.setTitle(movie);
        appearance.setCast(actor);
        appearance.setCharacters("Cobb");
        titleCastRepository.save(appearance);
    }

    @Test
    void shouldGetMoviesList() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id").value(movieId.toString()))
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].year").value(2010));
    }

    @Test
    void shouldGetMoviesPageExtended() throws Exception {
        mockMvc.perform(get("/movies?extended=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].title").value("Inception"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetMovieById() throws Exception {
        mockMvc.perform(get("/movies/" + movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieId.toString()))
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void shouldReturn404ForNonExistentMovie() throws Exception {
        mockMvc.perform(get("/movies/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetActorsList() throws Exception {
        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").value("Leonardo DiCaprio"))
                .andExpect(jsonPath("$[0].birthYear").value(1974));
    }

    @Test
    void shouldGetActorById() throws Exception {
        mockMvc.perform(get("/actors/" + actorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(actorId.toString()))
                .andExpect(jsonPath("$.name").value("Leonardo DiCaprio"));
    }

    @Test
    void shouldGetAppearancesForActor() throws Exception {
        mockMvc.perform(get("/actors/" + actorId + "/appearances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].movie_name").value("Inception"))
                .andExpect(jsonPath("$[0].character_name").value("Cobb"))
                .andExpect(jsonPath("$[0].year").value(2010));
    }

    @Test
    void shouldSearchMoviesByName() throws Exception {
        mockMvc.perform(get("/movies?name=Incept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void shouldSearchActorsByName() throws Exception {
        mockMvc.perform(get("/actors?name=Leonardo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Leonardo DiCaprio"));
    }
}
