#!/bin/bash
set -e

databases=(
  security_db
  promotion_command_db
  promotion_query_db
  product_db
  notification_db
  pricing_db
  order_db
  inventory_db
  audit_db
  admin_db
)

for db in "${databases[@]}"; do
  echo "Creating database: $db"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    SELECT 'CREATE DATABASE $db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$db')\gexec
EOSQL
done