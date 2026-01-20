package com.armdb.movie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class MovieIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @Autowired
    private com.armdb.movie.repository.TitleRepository titleRepository;

    @Test
    void shouldReturnMovies() throws Exception {
        com.armdb.movie.entity.Title title = new com.armdb.movie.entity.Title();
        title.setId(java.util.UUID.randomUUID());
        title.setPrimaryTitle("Integration Movie");
        title.setStartYear(2024);
        title.setTconst("tt9999999");
        titleRepository.save(title);

        // Test Default (Array)
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].title").value("Integration Movie"));
                
        // Test Extended (Page)
        mockMvc.perform(get("/movies?extended=true"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.content[0].title").value("Integration Movie"));
    }

    @Test
    void shouldReturnActors() throws Exception {
        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk());
    }
}
