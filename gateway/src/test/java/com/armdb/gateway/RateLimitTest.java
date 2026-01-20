package com.armdb.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWebTestClient
class RateLimitTest {

    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379);

    @Autowired
    private WebTestClient webClient;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.armdb.gateway.filter.AuthFilter authFilter;

    @Test
    void rateLimit_ShouldBlockAfterLimitExceeded() {
        // Mock AuthFilter to allow requests through
        org.mockito.Mockito.when(authFilter.filter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> {
                    org.springframework.web.server.ServerWebExchange exchange = invocation.getArgument(0);
                    org.springframework.cloud.gateway.filter.GatewayFilterChain chain = invocation.getArgument(1);
                    return chain.filter(exchange);
                });
        
        // Also need to ensure AuthFilter order doesn't break things if mocked? 
        // MockBean replaces the bean, order might be lost if implementation doesn't have @Order on class vs method?
        // AuthFilter has @Order on class (impl Ordered). 
        // But MockBean might not proxy that interface method unless specified.
        // Actually, just ensuring it calls chain.filter is enough for valid functionality.
        // KeyResolver uses 'Principal' which AuthFilter *would* have populated if we were real.
        // If Principal is empty, KeyResolver defaults to 'anonymous'.
        // So this should work.

        for (int i = 0; i < 5; i++) {
            webClient.get().uri("/movies")
                    .exchange()
                    // 503 Service Unavailable (Movie Service down) is expected,
                    // OR 200 if we mocked Movie Service.
                    // We just want to ensure it's NOT 429 yet.
                    .expectStatus().is5xxServerError(); 
        }

        // 6th request should be rate limited
        webClient.get().uri("/movies")
                .exchange()
                .expectStatus().isEqualTo(429);
    }
}
