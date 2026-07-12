$ns="retail-promotion-platform"

Write-Host ""
Write-Host "Nodes"
kubectl get nodes

Write-Host ""
Write-Host "Pods"
kubectl get pods -n $ns

Write-Host ""
Write-Host "Deployments"
kubectl get deployments -n $ns

Write-Host ""
Write-Host "Services"
kubectl get svc -n $ns

Write-Host ""
Write-Host "Ingress"
kubectl get ingress -n $ns

Write-Host ""
Write-Host "PVC"
kubectl get pvc -n $ns

Write-Host ""
Write-Host "StatefulSets"
kubectl get statefulsets -n $ns

Write-Host ""
Write-Host "=================================="
Write-Host "Verification Complete"
Write-Host "=================================="