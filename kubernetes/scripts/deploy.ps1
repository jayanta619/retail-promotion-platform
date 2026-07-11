$ns = "retail-promotion-platform"

Write-Host ""
Write-Host "========================================="
Write-Host "Retail Promotion Platform Deployment"
Write-Host "========================================="

# Namespace
Write-Host ""
Write-Host "Creating Namespace..."
kubectl apply -f kubernetes/namespace.yaml

# PostgreSQL
Write-Host ""
Write-Host "Deploying PostgreSQL..."
kubectl apply -f kubernetes/infrastructure/postgres.yaml
kubectl rollout status statefulset/postgres -n $ns

# Infrastructure
Write-Host ""
Write-Host "Deploying ZooKeeper..."
kubectl apply -f kubernetes/infrastructure/zookeeper.yaml
kubectl rollout status deployment/zookeeper -n $ns

Write-Host ""
Write-Host "Deploying Kafka..."
kubectl apply -f kubernetes/infrastructure/kafka.yaml
kubectl rollout status deployment/kafka -n $ns

Write-Host ""
Write-Host "Deploying Redis..."
kubectl apply -f kubernetes/infrastructure/redis.yaml
kubectl rollout status deployment/redis -n $ns

Write-Host ""
Write-Host "Deploying Elasticsearch..."
kubectl apply -f kubernetes/infrastructure/elasticsearch.yaml
kubectl rollout status statefulset/elasticsearch -n $ns

Write-Host ""
Write-Host "Deploying Zipkin..."
kubectl apply -f kubernetes/infrastructure/zipkin.yaml
kubectl rollout status deployment/zipkin -n $ns

Write-Host ""
Write-Host "Deploying Prometheus..."
kubectl apply -f kubernetes/infrastructure/prometheus.yaml
kubectl rollout status deployment/prometheus -n $ns

Write-Host ""
Write-Host "Deploying Grafana..."
kubectl apply -f kubernetes/infrastructure/grafana.yaml
kubectl rollout status deployment/grafana -n $ns

Write-Host ""
Write-Host "Infrastructure Ready"

# Config Server
Write-Host ""
Write-Host "Deploying Config Server..."
kubectl apply -f kubernetes/config-server-deployment.yaml
kubectl rollout status deployment/config-server -n $ns

# Discovery Server
Write-Host ""
Write-Host "Deploying Discovery Server..."
kubectl apply -f kubernetes/discovery-server-deployment.yaml
kubectl rollout status deployment/discovery-server -n $ns

# Security Service
Write-Host ""
Write-Host "Deploying Security Service..."
kubectl apply -f kubernetes/security-service-deployment.yaml
kubectl rollout status deployment/security-service -n $ns

# API Gateway
Write-Host ""
Write-Host "Deploying API Gateway..."
kubectl apply -f kubernetes/api-gateway-deployment.yaml
kubectl rollout status deployment/api-gateway -n $ns

# Remaining Services
Write-Host ""
Write-Host "Deploying Remaining Microservices..."

$files = @(
    "product-service-deployment.yaml",
    "inventory-service-deployment.yaml",
    "pricing-service-deployment.yaml",
    "order-service-deployment.yaml",
    "notification-service-deployment.yaml",
    "audit-service-deployment.yaml",
    "admin-service-deployment.yaml",
    "promotion-command-service-deployment.yaml",
    "promotion-query-service-deployment.yaml"
)

foreach ($file in $files) {

    Write-Host ""
    Write-Host "Deploying $file"

    kubectl apply -f "kubernetes/$file"

}

Write-Host ""
Write-Host "Waiting for all deployments..."

kubectl rollout status deployment/product-service -n $ns
kubectl rollout status deployment/inventory-service -n $ns
kubectl rollout status deployment/pricing-service -n $ns
kubectl rollout status deployment/order-service -n $ns
kubectl rollout status deployment/notification-service -n $ns
kubectl rollout status deployment/audit-service -n $ns
kubectl rollout status deployment/admin-service -n $ns
kubectl rollout status deployment/promotion-command-service -n $ns
kubectl rollout status deployment/promotion-query-service -n $ns

# Ingress
Write-Host ""
Write-Host "Deploying API Gateway Ingress..."

kubectl apply -f kubernetes/infrastructure/ingres.yaml

Write-Host ""
Write-Host "========================================="
Write-Host "Deployment Completed Successfully"
Write-Host "========================================="

Write-Host ""
kubectl get pods -n $ns