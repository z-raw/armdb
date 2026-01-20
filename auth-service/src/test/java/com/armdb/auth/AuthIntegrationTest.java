package com.armdb.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn401WhenNoCredentials() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUserWhenCredentialsValid() throws Exception {
        mockMvc.perform(get("/user").with(httpBasic("user1", "pass1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("user1"));
    }

    @Test
    void shouldNotLeakPasswordInResponseBodyWhenCredentialsValid() throws Exception {
        mockMvc.perform(get("/user").with(httpBasic("user1", "pass1")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("pass1"))));
    }

    @Test
    void shouldReturn401WhenCredentialsInvalid() throws Exception {
        mockMvc.perform(get("/user").with(httpBasic("user1", "wrongpass")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAccessToSwaggerDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowAccessToActuator() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
