#!/usr/bin/env bash

set -euo pipefail

GIT_SHA="${1:?Git SHA is required}"
ACR_LOGIN_SERVER="${2:?ACR login server is required}"
OUTPUT_DIR="${3:-rendered-manifests}"

SOURCE_DIR="kubernetes"

if [[ ! -d "${SOURCE_DIR}" ]]; then
  echo "ERROR: Kubernetes directory not found: ${SOURCE_DIR}"
  exit 1
fi

rm -rf "${OUTPUT_DIR}"
mkdir -p "${OUTPUT_DIR}"

echo "Rendering application manifests..."
echo "Source: ${SOURCE_DIR}"
echo "Output: ${OUTPUT_DIR}"
echo "ACR: ${ACR_LOGIN_SERVER}"
echo "Image tag: ${GIT_SHA}"

while IFS= read -r -d '' manifest; do
  relative_path="${manifest#${SOURCE_DIR}/}"
  destination="${OUTPUT_DIR}/${relative_path}"

  mkdir -p "$(dirname "${destination}")"

  sed \
    -e "s|retailplatformacr\.azurecr\.io/\([^:[:space:]]*\):[^[:space:]\"']*|${ACR_LOGIN_SERVER}/\1:${GIT_SHA}|g" \
    -e "s|REPLACE_WITH_GIT_SHA|${GIT_SHA}|g" \
    -e "s|REPLACE_WITH_ACR_LOGIN_SERVER|${ACR_LOGIN_SERVER}|g" \
    "${manifest}" > "${destination}"

  echo "Rendered: ${relative_path}"
done < <(
  find "${SOURCE_DIR}" \
    -path "${SOURCE_DIR}/infrastructure" -prune -o \
    -type f \
    \( -name "*-deployment.yaml" -o -name "*-deployment.yml" -o -name "namespace.yaml" -o -name "*-ingress.yaml" -o -name "*-ingress.yml" \) \
    -print0
)

manifest_count="$(
  find "${OUTPUT_DIR}" \
    -type f \
    \( -name "*.yaml" -o -name "*.yml" \) |
  wc -l |
  tr -d ' '
)"

if [[ "${manifest_count}" -eq 0 ]]; then
  echo "ERROR: No application manifests were rendered."
  exit 1
fi

echo "Successfully rendered ${manifest_count} application manifests."