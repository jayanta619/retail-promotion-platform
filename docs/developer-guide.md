# Developer Guide

## Package Structure Per Service

Each business microservice follows this internal layout:

- `controller` — REST endpoints, request/response mapping only, no business logic
- `service` — business logic and transaction boundaries
- `repository` — Spring Data JPA or Elasticsearch repositories
- `entity` — JPA entities mapped to database tables
- `dto` — request/response objects exposed over HTTP
- `event` — Kafka event payloads
- `producer` / `consumer` — Kafka publishing and listening components
- `config` — Spring `@Configuration` classes (security, cache, etc.)

## Why Shared Libraries Exist

`common-model`, `common-exception`, `common-security`, and `common-utils` are Maven modules that every service depends on. This avoids duplicating the same `ApiResponse` wrapper, exception handler, or JWT validation logic across 13 services. When the response envelope format changes, it changes in one library, and every service picks it up on its next build.

## Adding a New Microservice

1. Create a new module under `services/`.
2. Add it to the root `pom.xml` `<modules>` list.
3. Depend on `common-model` and `common-exception` at minimum.
4. Register with Eureka via `spring-cloud-starter-netflix-eureka-client`.
5. Add a Dockerfile following the existing pattern (`eclipse-temurin:21-jre-alpine`).
6. Add the service to `docker/docker-compose.yml` and `kubernetes/<name>-deployment.yaml`.

## Request Lifecycle Example: Creating a Promotion

1. Client calls `POST /api/v1/promotions/commands` on the API Gateway.
2. Gateway's `JwtAuthenticationFilter` validates the JWT and injects `X-User-Id` / `X-User-Roles` headers.
3. Gateway's `CorrelationIdFilter` stamps an `X-Correlation-Id` if not present.
4. Gateway routes to `promotion-command-service` via Eureka-resolved load balancing (`lb://promotion-command-service`).
5. `PromotionCommandController` validates the request body (`@Valid`) and delegates to `PromotionCommandService`.
6. The service persists the `Promotion` entity via `PromotionRepository` inside a `@Transactional` boundary.
7. After a successful commit, `PromotionEventPublisher` sends a `PromotionCreatedEvent` to Kafka.
8. `promotion-query-service`'s `PromotionEventConsumer` picks up the event asynchronously and indexes it into Elasticsearch.
9. Subsequent `GET` requests for that promotion are served from Elasticsearch, with Redis caching hot lookups.

## Security Flow

1. Client calls `POST /api/v1/auth/register` then `POST /api/v1/auth/login`.
2. `AuthService` verifies the password hash (BCrypt) and issues an access token (15 min) and refresh token (7 days) via `JwtTokenProvider`.
3. Every subsequent request includes `Authorization: Bearer <accessToken>`.
4. The Gateway validates the signature and expiry before forwarding the request; downstream services trust the `X-User-Id`/`X-User-Roles` headers set by the Gateway.
