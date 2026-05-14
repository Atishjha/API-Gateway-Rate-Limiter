package com.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RateLimitingTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void rateLimiterShouldEventuallyReturn429() {
        // This test requires a real Redis instance and a route with RequestRateLimiter.
        // It's an example skeleton. In a real project you would use Testcontainers.
        for (int i = 0; i < 20; i++) {
            webTestClient.get().uri("/anything")
                    .exchange()
                    .expectStatus().isEqualTo(i < 10 ?
                            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR : // no backend
                            org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
        }
    }
}