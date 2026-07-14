param(
    [Parameter(Mandatory = $true)]
    [string]$GitHubOwner,

    [Parameter(Mandatory = $true)]
    [string]$GitHubRepo,

    [string]$AppName = "github-retail-platform-cicd",
    [string]$ResourceGroup = "retail-rg",
    [string]$AksName = "retail-aks",
    [string]$AcrName = "retailplatformacr",
    [string]$EnvironmentName = "aks-production"
)

$ErrorActionPreference = "Stop"

function Invoke-AzTsv {
    param([Parameter(ValueFromRemainingArguments = $true)][string[]]$Arguments)
    $result = & az @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "Azure CLI failed: az $($Arguments -join ' ')"
    }
    return ($result | Out-String).Trim()
}

Write-Host "Checking Azure login..."
$subscriptionId = Invoke-AzTsv account show --query id --output tsv
$tenantId = Invoke-AzTsv account show --query tenantId --output tsv

Write-Host "Finding or creating Microsoft Entra application: $AppName"
$appId = Invoke-AzTsv ad app list --display-name $AppName --query "[0].appId" --output tsv
if ([string]::IsNullOrWhiteSpace($appId)) {
    $appId = Invoke-AzTsv ad app create --display-name $AppName --query appId --output tsv
}
$appObjectId = Invoke-AzTsv ad app show --id $appId --query id --output tsv

$servicePrincipalObjectId = Invoke-AzTsv ad sp list --filter "appId eq '$appId'" --query "[0].id" --output tsv
if ([string]::IsNullOrWhiteSpace($servicePrincipalObjectId)) {
    $servicePrincipalObjectId = Invoke-AzTsv ad sp create --id $appId --query id --output tsv
}

function Ensure-FederatedCredential {
    param(
        [string]$Name,
        [string]$Subject
    )

    $existing = Invoke-AzTsv ad app federated-credential list --id $appObjectId --query "[?name=='$Name'].name | [0]" --output tsv
    if (-not [string]::IsNullOrWhiteSpace($existing)) {
        Write-Host "Federated credential already exists: $Name"
        return
    }

    $file = Join-Path $env:TEMP "$Name.json"
    @{
        name        = $Name
        issuer      = "https://token.actions.githubusercontent.com"
        subject     = $Subject
        description = "GitHub Actions OIDC for $Subject"
        audiences   = @("api://AzureADTokenExchange")
    } | ConvertTo-Json -Depth 5 | Set-Content -Path $file -Encoding utf8

    & az ad app federated-credential create --id $appObjectId --parameters $file | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Could not create federated credential $Name"
    }
    Remove-Item $file -Force
    Write-Host "Created federated credential: $Name"
}

# Build/push jobs run directly from the main branch.
Ensure-FederatedCredential `
    -Name "github-main" `
    -Subject "repo:$GitHubOwner/$GitHubRepo`:ref:refs/heads/main"

# Deployment job uses a protected GitHub Environment.
Ensure-FederatedCredential `
    -Name "github-aks-production" `
    -Subject "repo:$GitHubOwner/$GitHubRepo`:environment:$EnvironmentName"

$acrId = Invoke-AzTsv acr show --name $AcrName --query id --output tsv
$aksId = Invoke-AzTsv aks show --resource-group $ResourceGroup --name $AksName --query id --output tsv

function Ensure-RoleAssignment {
    param(
        [string]$Role,
        [string]$Scope
    )

    $existing = Invoke-AzTsv role assignment list `
        --assignee-object-id $servicePrincipalObjectId `
        --scope $Scope `
        --query "[?roleDefinitionName=='$Role'].id | [0]" `
        --output tsv

    if ([string]::IsNullOrWhiteSpace($existing)) {
        & az role assignment create `
            --assignee-object-id $servicePrincipalObjectId `
            --assignee-principal-type ServicePrincipal `
            --role $Role `
            --scope $Scope | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "Could not assign role '$Role'"
        }
        Write-Host "Assigned role: $Role"
    }
    else {
        Write-Host "Role already assigned: $Role"
    }
}

Ensure-RoleAssignment -Role "AcrPush" -Scope $acrId

# This broad role is used because the present workflow calls aks-set-context with admin=true.
# Replace it later with Entra-enabled cluster-user access plus namespace-scoped Kubernetes RBAC.
Ensure-RoleAssignment -Role "Azure Kubernetes Service Cluster Admin Role" -Scope $aksId

Write-Host ""
Write-Host "Create these GitHub repository secrets:"
Write-Host "AZURE_CLIENT_ID=$appId"
Write-Host "AZURE_TENANT_ID=$tenantId"
Write-Host "AZURE_SUBSCRIPTION_ID=$subscriptionId"
Write-Host ""
Write-Host "Create GitHub Environment: $EnvironmentName"
Write-Host "Limit it to branch main and add a required reviewer."
Write-Host ""
Write-Host "Check that AKS can pull from ACR:"
Write-Host "az aks check-acr -g $ResourceGroup -n $AksName --acr $AcrName.azurecr.io"
