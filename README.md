# Retail Promotion Platform

An original, enterprise-grade retail promotion platform built with Java 21, Spring Boot 3, and a microservices architecture inspired by large-scale retail systems. This project is designed as a hands-on learning environment for enterprise Java patterns — it is not a CRUD tutorial, and it is not copied from any proprietary codebase.

## What This Platform Does

The platform models how a large retailer creates, manages, and serves promotional campaigns (discounts, BOGO offers, bundles) across stores and pricing zones, while staying consistent with inventory, pricing, and order systems in near real time.

## Architecture Overview

The platform uses **CQRS** (Command Query Responsibility Segregation) for promotions: writes go through the Promotion Command Service into PostgreSQL, and are propagated via **Kafka** events to the Promotion Query Service, which projects them into **Elasticsearch** for fast, flexible search and caches hot reads in **Redis**.

| Layer | Technology |
|---|---|
| API entry point | Spring Cloud Gateway |
| Service discovery | Netflix Eureka |
| Centralized config | Spring Cloud Config |
| Write-side database | PostgreSQL + Flyway |
| Read-side search | Elasticsearch |
| Caching | Redis |
| Messaging | Apache Kafka |
| Object storage | MinIO (local) / Azure Blob (cloud) |
| Auth | JWT + Spring Security |
| Resilience | Resilience4j (circuit breaker, retry, rate limiter) |
| Observability | Micrometer, Prometheus, Grafana, Zipkin |
| Containerization | Docker, Docker Compose |
| Orchestration | Kubernetes, Helm |
| Cloud target | Azure |

## Microservices

- **api-gateway** — single entry point, JWT validation, routing, correlation ID propagation
- **discovery-server** — Eureka service registry
- **config-server** — centralized configuration
- **security-service** — user registration, login, JWT issuance, RBAC
- **promotion-command-service** — promotion writes, publishes domain events to Kafka
- **promotion-query-service** — promotion reads via Elasticsearch, Redis-cached
- **product-service** — product catalog
- **inventory-service** — stock levels per store
- **pricing-service** — price rules and calculations
- **order-service** — order placement and lifecycle
- **notification-service** — customer/store notifications
- **audit-service** — immutable audit trail across the platform
- **admin-service** — back-office administration APIs

## Repository Structure

```
retail-promotion-platform/
├── services/            # All microservices
├── libraries/           # Shared libraries (common-model, common-security, common-exception, common-utils)
├── docker/              # docker-compose.yml and supporting configs
├── kubernetes/          # Raw Kubernetes manifests
├── docs/                # Architecture, diagrams, runbook, developer guide
├── postman/             # Postman collection for manual API testing
└── .github/workflows/   # CI pipeline
```

## Prerequisites

- Java 21 (LTS)
- Maven 3.9+
- Docker Desktop
- kubectl (for Kubernetes)
- Azure CLI (for cloud deployment)
- Git

## Quick Start (Local, Docker Compose)

```bash
git clone https://github.com/jayanta619/retail-promotion-platform.git
cd retail-promotion-platform

mvn clean install -DskipTests

cd docker
docker-compose up --build
```

Once containers are healthy, verify:

- Eureka dashboard: http://localhost:8761
- API Gateway health: http://localhost:8080/actuator/health
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
- Zipkin: http://localhost:9411
- MinIO console: http://localhost:9001

See `docs/runbook.md` for full step-by-step verification and `docs/developer-guide.md` for a deep explanation of every layer.

## Development Phases

This project was built incrementally, phase by phase, exactly as a real engineering team would:

1. Phase 1 — Discovery Server, Config Server, API Gateway, Docker Compose foundation
2. Phase 2 — Security Service (JWT auth, RBAC)
3. Phase 3 — Promotion Command + Query Services (CQRS, Kafka, Elasticsearch, Redis)
4. Phase 4 — Product, Inventory, Pricing, Order, Notification, Audit, Admin services
5. Phase 5 — Kubernetes, Helm, CI/CD, Azure deployment

## License

This is an original educational project. No proprietary code from any company is included.
