# Runbook

## Starting the Platform Locally

```bash
cd docker
docker-compose up --build
```

Wait roughly 60-90 seconds for all infrastructure containers (Postgres, Kafka, Elasticsearch) to become healthy before the Spring Boot services finish registering with Eureka.

## Health Check Checklist

| Service | Check |
|---|---|
| Eureka | http://localhost:8761 shows all services registered |
| API Gateway | curl http://localhost:8080/actuator/health returns UP |
| Security Service | curl http://localhost:8081/actuator/health returns UP |
| Promotion Command | curl http://localhost:8082/actuator/health returns UP |
| Promotion Query | curl http://localhost:8083/actuator/health returns UP |
| Kafka | docker logs retail-kafka shows started |
| Elasticsearch | curl http://localhost:9200 returns cluster info |

## Common Issues

- If a service fails to start with a datasource connection error, confirm Postgres is fully up (`docker logs retail-postgres`) before the dependent service starts; Docker Compose `depends_on` only waits for container start, not readiness.
- If Eureka shows a service as DOWN, check that the service's `application.yml` points to `http://localhost:8761/eureka/` when running outside Docker, or `http://discovery-server:8761/eureka/` when running inside Docker Compose.
- If Flyway migration fails, drop and recreate the specific database and rerun `docker-compose up`.

## Stopping and Cleaning Up

```bash
docker-compose down -v
```

The `-v` flag removes named volumes (Postgres and MinIO data), giving you a completely clean slate.
