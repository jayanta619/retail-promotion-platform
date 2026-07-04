# High-Level Architecture

## Request Flow

```
Client
  -> API Gateway (JWT validation, correlation ID, routing)
    -> Security Service (auth only)
    -> Promotion Command Service (writes) --Kafka--> Promotion Query Service (reads, Elasticsearch, Redis)
    -> Product / Inventory / Pricing / Order Services
```

## CQRS Flow for Promotions

1. Client sends `POST /api/v1/promotions/commands` through the Gateway.
2. Promotion Command Service validates and persists the promotion in PostgreSQL.
3. Command Service publishes a `PromotionCreatedEvent` to the `promotion.created.v1` Kafka topic.
4. Promotion Query Service consumes the event and projects it into an Elasticsearch document.
5. Client reads promotions via `GET /api/v1/promotions/{id}` or `/search`, served from Elasticsearch and cached in Redis.

## Why CQRS Here

Promotion writes are relatively low-volume and transactional (need strong consistency), while promotion reads are extremely high-volume (every storefront page load may check active promotions) and benefit from a search-optimized, denormalized, cache-friendly store. Splitting these into two services lets each scale independently.

## Deployment Topology

- Local: Docker Compose runs all services plus infra (Postgres, Kafka, Redis, Elasticsearch, MinIO, Prometheus, Grafana, Zipkin) on a single Docker network.
- Kubernetes: Each service is a Deployment + ClusterIP Service; API Gateway is exposed via Ingress; Postgres runs as a StatefulSet with persistent volumes.
- Azure: Kubernetes manifests are adapted to AKS; MinIO is replaced with Azure Blob Storage; secrets move to Azure Key Vault.
