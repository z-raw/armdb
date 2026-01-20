<<<<<<< HEAD
# ARMDB - Advanced Actor & Movie Database

ARMDB is a comprehensive microservices-based application for exploring movies and actors. It provides a robust backend API and a modern, interactive frontend for searching and viewing detailed information about cinematic works and the people behind them.

## Architecture

ARMDB is built using a Microservices Architecture to ensure scalability, flexibility, and fault isolation.
=======
# ArMDb 

ArMDb is a microservices-based application for exploring movies and actors using the IMDb dataset limited to the last 5 years. 
Its goal is to provide low-latency, scalable backend APIs and a simple frontend with basic searching and viewing functionalities.

## Architecture

Built using a Microservices Architecture to ensure scalability, flexibility, and fault isolation.
>>>>>>> a43eb40 (doc: add README.md)

### Components

The system consists of the following core services:

1.  **Gateway Service** (Port 8080)
    -   **Entry Point**: All client requests go through the Gateway.
    -   **Routing**: Dynamically routes requests to `movie-service` or `auth-service`.
    -   **Rate Limiting**: Uses Redis to limit requests (5 req/min per user) to prevent abuse.
    -   **Authentication**: Validates users via Basic Auth and manages session security.
    -   **Frontend**: Serves the static `index.html` UI.

2.  **Registry Service (Eureka)** (Port 8761)
    -   **Service Discovery**: Allows services to register themselves and discover others dynamically without hardcoded URLs.

3.  **Movie Service** (Port 6000)
    -   **Core Logic**: Manages data for Movies, Actors, and their relationships.
    -   **Database**: Connects to the PostgreSQL database (`armdb`).
    -   **Search**: Implements full-text search for actors and keyword search for movies.

4.  **Auth Service** (Port 5000)
    -   **Security**: Handles user verification and provides user details for the Gateway's auth filter.

### Infrastructure
-   **PostgreSQL**: Relational database storing movie/actor data with optimized indices (GIN for search).
-   **Redis**: In-memory data store used by the Gateway for distributed rate limiting.
-   **Docker**: All services are containerized for consistent deployment.

### Architecture Diagram

```mermaid
graph TD
    Client[Client / Browser] -->|HTTP Request| Gateway[Gateway Service :8080]
    Gateway -->|Rate Limit Check| Redis((Redis))
    Gateway -->|Service Lookup| Registry[Eureka Registry :8761]
    Gateway -->|/movies, /actors| MovieService[Movie Service :6000]
    Gateway -->|/uaa/user| AuthService[Auth Service :5000]
    MovieService -->|Query| DB((PostgreSQL))
```

## Design

### Why Microservices?
<<<<<<< HEAD
-   **Scalability**: The `movie-service` (high read traffic) can be scaled independently of the `auth-service` (lower traffic).
=======
-   **Scalability**: The `movie-service` (expected high read traffic) can be scaled independently of the `auth-service` (lower traffic).
>>>>>>> a43eb40 (doc: add README.md)
-   **Resilience**: Issues in the Auth service won't crash the Movie catalog functions (though access might be restricted).
-   **Technology Independence**: Different services can evolve with different libraries or Java versions if needed.

### Service Discovery (Eureka)
We use Netflix Eureka for client-side load balancing and decoupling. Services register correctly on startup, allowing the Gateway to route traffic via `lb://service-name` rather than static IPs.

### Gateway & Rate Limiting
A central API Gateway pattern simplifies the client (single endpoint) and offloads cross-cutting concerns:
-   **Security**: Auth is enforced at the edge.
<<<<<<< HEAD
-   **Rate Limiting**: Implemented using **Spring Cloud Gateway RequestRateLimiter** with Redis. This protects backend resources from being overwhelmed by a single user.
=======
-   **Rate Limiting**: Implemented using **Spring Cloud Gateway RequestRateLimiter** with Redis.
>>>>>>> a43eb40 (doc: add README.md)

## Dependencies

-   **Java 17 / 21**: Core language.
<<<<<<< HEAD
-   **Spring Boot 3.x**: Framework for microservices.
-   **Spring Cloud Gateway**: Routing and filtering.
=======
-   **Spark 3.5.0**: ETL and cleansing of the IMDb dataset
-   **Spring Boot 3.2**: For the microservices.
-   **Spring Cloud Gateway 2023.0.**: Routing and filtering.
>>>>>>> a43eb40 (doc: add README.md)
-   **Spring Cloud Netflix Eureka**: Service Registry.
-   **Spring Data JPA**: Database abstraction.
-   **PostgreSQL Driver**: DB connectivity.
-   **Testcontainers**: Integration testing with real containers.
<<<<<<< HEAD
-   **Lombok**: Reduces boilerplate code.
=======
>>>>>>> a43eb40 (doc: add README.md)
-   **OpenAPI (Swagger)**: API documentation.

## How to Run

### Prerequisites
-   Docker & Docker Compose
<<<<<<< HEAD
-   Java 17+ (optional, for local dev)
-   Maven (optional, for local dev)

### Quick Start
1.  Clone the repository.
2.  Run the entire stack with Docker Compose:
=======
-   Java 17+
-   Maven

### Quick Start
1.  Clone the repository.
2.  Build the services and/or run the tests
    ```bash
    mvn clean package [-DskipTests]    
    ```
3.  Run the entire stack with Docker Compose:
>>>>>>> a43eb40 (doc: add README.md)
    ```bash
    docker-compose up --build
    ```
3.  Access the application:
<<<<<<< HEAD
    -   **Frontend**: [http://localhost:8080](http://localhost:8080)
    -   **Swagger UI**: [http://localhost:6000/swagger-ui.html](http://localhost:6000/swagger-ui.html)
    -   **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)

## Tests & Coverage

We prioritize robust testing to ensure reliability.

### Test Strategy
1.  **Unit Tests**: Verify business logic in isolation.
2.  **Integration Tests**: Use **Testcontainers** (running real Postgres/Redis in Docker) to verify database interactions and API contracts.
    -   `MovieIntegrationTest`: Checks API endpoints and DB persistence.
    -   `RateLimitTest`: Verifies Gateway blocks excessive requests.
3.  **End-to-End Tests**: Manual verification and scripted checks of the full flow.

### Running Tests
```bash
# Run tests for Movie Service
cd movie-service
mvn test

# Run tests for Gateway
cd gateway
mvn test
```
=======
    -   **Frontend/Gateway**: [http://localhost:8080](http://localhost:8080)
    -   **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)
    -   **Prometheus**:       [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)
>>>>>>> a43eb40 (doc: add README.md)

## UI Showcase

### Home Page
<<<<<<< HEAD
The landing page with a modern dark-mode design.
![Home Page](docs/images/home.png)

### Search Results
Interactive search for actors and movies.
![Search Results](docs/images/search.png)

### Movie Details
Detailed view showing release year, genres, and more.
![Movie Details](docs/images/details.png)

### Login
Secure login popup for authentication.
![Login](docs/images/login.png)
=======
The landing page.
![Home Page](docs/images/home.png)

### Search Results
Search for actors and movies.
![Search Results](docs/images/search.png)

### Movie Details
![Movie Details](docs/images/details.png)

### Appearances
![Actors Appearances](docs/images/appearances.png)
>>>>>>> a43eb40 (doc: add README.md)
