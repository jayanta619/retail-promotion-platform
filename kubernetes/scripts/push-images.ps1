$acr="retailplatformacr.azurecr.io"

$services=@(
    "config-server",
    "discovery-server",
    "security-service",
    "api-gateway",
    "product-service",
    "inventory-service",
    "pricing-service",
    "order-service",
    "notification-service",
    "audit-service",
    "admin-service",
    "promotion-command-service",
    "promotion-query-service"
)

foreach($svc in $services){

    Write-Host ""
    Write-Host "======================================="
    Write-Host "Processing $svc"
    Write-Host "======================================="

    docker tag docker-$svc`:latest $acr/$svc`:v1

    docker push $acr/$svc`:v1

}

Write-Host ""
Write-Host "All Images Successfully Pushed."