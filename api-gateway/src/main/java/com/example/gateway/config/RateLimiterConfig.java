package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    /**
     * Rate limit by client IP address.
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            return Mono.just(ip);
        };
    }

    /**
     * Rate limit by authenticated user ID (set by JWT filter).
     * Falls back to "anonymous" if header not present.
     */
    @Bean
    public KeyResolver userIdKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            return Mono.justOrEmpty(userId).switchIfEmpty(Mono.just("anonymous"));
        };
    }
}