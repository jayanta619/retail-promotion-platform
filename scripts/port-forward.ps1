$ns = "retail-promotion-platform"

Write-Host ""
Write-Host "========================================="
Write-Host "Starting Retail Platform Port Forwarding"
Write-Host "========================================="

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/api-gateway 8080:8080 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/config-server 8888:8888 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/discovery-server 8761:8761 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/postgres 5432:5432 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/redis 6379:6379 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/kafka 9092:9092 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/elasticsearch 9200:9200 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/prometheus 9090:9090 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/grafana 3000:3000 -n $ns"

Start-Process powershell -ArgumentList "-NoExit", "-Command", "kubectl port-forward svc/zipkin 9411:9411 -n $ns"

Write-Host ""
Write-Host "========================================="
Write-Host "All Port Forwards Started"
Write-Host "========================================="

Write-Host ""
Write-Host "Open these URLs:"
Write-Host "API Gateway       : http://localhost:8080"
Write-Host "Config Server     : http://localhost:8888"
Write-Host "Eureka            : http://localhost:8761"
Write-Host "Elasticsearch     : http://localhost:9200"
Write-Host "Prometheus        : http://localhost:9090"
Write-Host "Grafana           : http://localhost:3000"
Write-Host "Zipkin            : http://localhost:9411"

Write-Host ""
Write-Host "PostgreSQL        : localhost:5432"
Write-Host "Redis             : localhost:6379"
Write-Host "Kafka             : localhost:9092"