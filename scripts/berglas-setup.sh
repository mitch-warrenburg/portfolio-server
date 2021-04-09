#!/usr/bin/env sh

# -- This script will setup the resources needed for using berglas with the mutating webhook --

PROJECT_ID="warrenburg-portfolio";

# setup berglas
gcloud iam service-accounts create berglas-accessor \
  --project ${PROJECT_ID} \
  --display-name "Berglas secret accessor account"

SA_EMAIL="berglas-accessor@${PROJECT_ID}.iam.gserviceaccount.com"

berglas grant "sm://${PROJECT_ID}/my-secret" --member "serviceAccount:$SA_EMAIL"
kubectl create serviceaccount "envserver"


gcloud iam service-accounts add-iam-policy-binding \
  --project ${PROJECT_ID} \
  --role "roles/iam.workloadIdentityUser" \
  --member "serviceAccount:${PROJECT_ID}.svc.id.goog[default/envserver]" \
  "berglas-accessor@${PROJECT_ID}.iam.gserviceaccount.com"

kubectl annotate serviceaccount "envserver" \
  "iam.gke.io/gcp-service-account=berglas-accessor@${PROJECT_ID}.iam.gserviceaccount.com"
