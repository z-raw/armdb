package com.armdb.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver userKeyResolver() {

        return exchange -> exchange.getPrincipal()
                .map(Principal::getName)
                .switchIfEmpty(Mono.defer(() -> {
                    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                    if (authHeader != null && authHeader.startsWith("Basic ")) {
                        try {
                            String base64Credentials = authHeader.substring("Basic ".length());
                            byte[] decoded = java.util.Base64.getDecoder().decode(base64Credentials);
                            String credentials = new String(decoded);
                            String[] parts = credentials.split(":", 2);
                            
                            return Mono.just(parts[0]);
                        } catch (Exception e) {
                            return Mono.just("anonymous");
                        }
                    }
                    return Mono.just("anonymous");
                }));
    }
}
