# API-Gateway-Rate-Limiter
A production-style API Gateway built using Java and Spring Boot that demonstrates advanced backend engineering concepts including authentication, distributed rate limiting, reverse proxying, request tracing, resilience patterns, and observability.
Features
JWT Authentication & Authorization
Reverse Proxy Routing
Token Bucket Rate Limiting
IP-based Throttling
Request Logging
Circuit Breaker Pattern
Request Tracing
Dynamic Routing
Load Balancing
Metrics Dashboard
Distributed Redis Caching
Docker Support
Prometheus + Grafana Monitoring
Tech Stack
Technology	Purpose
Java 21	Core Language
Spring Boot 3	Backend Framework
Spring Cloud Gateway	API Gateway
Spring Security	Authentication
JWT	Token-based Security
Redis	Distributed Cache & Rate Limiting
Bucket4j	Token Bucket Algorithm
Resilience4j	Circuit Breaker
Micrometer	Metrics Collection
Prometheus	Monitoring
Grafana	Visualization
Docker	Containerization
System Architecture
                +------------------+
                |      Client      |
                +------------------+
                          |
                          v
                +------------------+
                |    API Gateway   |
                +------------------+
                  |    |     |   |
                  |    |     |   |
                  |    |     |   +--> Logging
                  |    |     +------> Rate Limiter
                  |    +------------> JWT Auth
                  +-----------------> Circuit Breaker
                          |
                          v
          +--------------------------------+
          |        Reverse Proxy Layer     |
          +--------------------------------+
               |                |
               v                v
      +----------------+   +----------------+
      | User Service   |   | Order Service  |
      +----------------+   +----------------+


Project Structure 
api-gateway/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/gateway/
│   │   │   ├── ApiGatewayApplication.java           // @SpringBootApplication
│   │   │   ├── config/
│   │   │   │   └── RateLimiterConfig.java           // KeyResolver beans
│   │   │   ├── filter/
│   │   │   │   ├── JwtAuthGatewayFilterFactory.java  // JWT validation filter
│   │   │   │   └── RequestLoggingFilter.java         // global logging filter
│   │   │   ├── controller/
│   │   │   │   └── FallbackController.java           // circuit breaker fallback
│   │   │   ├── repository/
│   │   │   │   └── DatabaseRouteDefinitionRepository.java  // dynamic routes (optional)
│   │   └── resources/
│   │       ├── application.yml                       // all gateway & resilience configs
│   │       └── jwt-secret.txt                        // base64-encoded signing key (if needed)
│   └── test/
│       └── java/com/example/gateway/
│           ├── JwtAuthTest.java
│           └── RateLimitingTest.java
└── docker-compose.yml                                 // optional: Redis, Zipkin, etc 


Core Features Explained
JWT Authentication

Every incoming request is validated using JWT tokens before forwarding to downstream services.

Authentication Flow
Client Request
      ↓
JWT Filter
      ↓
Token Validation
      ↓
User Context Injection
      ↓
Forward Request
Token Bucket Rate Limiter

Implements distributed rate limiting using Redis + Bucket4j.

Example Policy
Capacity: 100 requests
Refill: 10 tokens/sec

If limit exceeds:

HTTP 429 Too Many Requests
Reverse Proxying

Routes requests dynamically to backend services.

Example Routes
/api/users/**   -> user-service
/api/orders/**  -> order-service
Circuit Breaker

Protects downstream services from cascading failures.

States
CLOSED
OPEN
HALF_OPEN

Powered by Resilience4j.

Request Tracing

Every request gets a unique trace ID.

Example
X-Trace-ID: 9f82hda12

Helps in:

Debugging
Distributed tracing
Monitoring
Metrics Dashboard

Monitor:

Request count
Error rate
Latency
Active connections
Service health

Using:

Prometheus
Grafana
Getting Started
Prerequisites
Java 21
Maven
Docker
Redis
Installation
Clone Repository
git clone https://github.com/your-username/api-gateway.git
cd api-gateway
Run Redis
docker run -p 6379:6379 redis
Build Project
mvn clean install
Run Application
mvn spring-boot:run
Configuration
application.yml
server:
  port: 8080

spring:
  redis:
    host: localhost
    port: 6379

jwt:
  secret: your-secret-key

gateway:
  rate-limit:
    capacity: 100
    refill-tokens: 10
Example API Request
Generate JWT
POST /auth/login
Access Protected Route
curl -H "Authorization: Bearer <token>" \
http://localhost:8080/api/users
Docker Support
Build Docker Image
docker build -t api-gateway .
Run Container
docker run -p 8080:8080 api-gateway
Monitoring Setup
Prometheus
scrape_configs:
  - job_name: 'gateway'
    metrics_path: '/actuator/prometheus'
Grafana Dashboard

Track:

API throughput
Error rates
Gateway latency
Circuit breaker status
Rate limit violations
Advanced Features
Distributed Rate Limiting
Dynamic Route Reloading
Adaptive Throttling
Canary Routing
Geo-based Filtering
Request Replay Protection
API Key Management
WebSocket Proxying
GraphQL Gateway
Performance Engineering

This project demonstrates:

Non-blocking reactive programming
High-throughput request handling
Distributed caching
Low-latency filtering
Fault tolerance
Horizontal scalability
Future Improvements
Kubernetes Deployment
Service Discovery with Eureka
AI-based anomaly detection
Multi-region deployment
OAuth2 Integration
Distributed tracing with Zipkin
Learning Outcomes

This project helps in understanding:

API Gateway Architecture
Networking Concepts
Middleware Design
Distributed Systems
Security Engineering
Resilience Patterns
Observability
High-performance backend systems
Author
Atish Jha

B.Tech CSE Student
Backend & Systems Engineering Enthusiast
