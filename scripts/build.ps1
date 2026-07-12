Write-Host "====================================="
Write-Host "Building Maven Project"
Write-Host "====================================="

mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0){
    Write-Host "Maven Build Failed"
    exit
}

Write-Host ""
Write-Host "Building Docker Images"

docker compose build

if ($LASTEXITCODE -ne 0){
    Write-Host "Docker Build Failed"
    exit
}

Write-Host ""
Write-Host "Build Completed Successfully"