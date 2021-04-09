#!/usr/bin/env sh

helm install portfolio-db \
  -n portfolio \
  --set postgresqlPassword=admin,postgresqlUsername=admin,postgresqlPostgresPassword=password,postgresqlDatabase=portfolio \
  bitnami/postgresql
