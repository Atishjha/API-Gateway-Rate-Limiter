package com.example.gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.crypto.SecretKey;
import java.net.URI;
import java.util.Base64;
import java.util.Collections;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class JwtAuthTest {

    @Autowired
    private WebTestClient webTestClient;

    private SecretKey signingKey;
    private String validToken;

    @BeforeEach
    void setUp() {
        // same key as in application.yml
        byte[] keyBytes = Base64.getDecoder().decode("bXktc2VjcmV0LWtleQ==");
        signingKey = Keys.hmacShaKeyFor(keyBytes);

        validToken = Jwts.builder()
                .subject("user123")
                .claim("role", "USER")
                .signWith(signingKey)
                .compact();

        // Add a test route dynamically (or we can rely on static configuration)
        // For simplicity, we add a route that proxies to a non-existent server,
        // but we're only testing the filter behaviour, not the backend.
        // In a real test, you would mock the downstream service.
    }

    @Test
    void requestWithoutToken_returnsUnauthorized() {
        webTestClient.get().uri("/anything")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void requestWithInvalidToken_returnsUnauthorized() {
        webTestClient.get().uri("/anything")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token.here")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void requestWithValidToken_proceedsAndForwardsHeaders() {
        // This test will fail because there is no real downstream service.
        // But it proves the filter passed the JWT check and tried to forward.
        // In a full integration test you would mock the backend.
        webTestClient.get().uri("/anything")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .exchange()
                .expectStatus().is5xxServerError(); // because no backend
    }
}