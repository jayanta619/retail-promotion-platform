$ns="retail-promotion-platform"

kubectl delete namespace $ns

kubectl wait --for=delete namespace/$ns --timeout=300s