#!/usr/bin/env sh

# -- This script will create the initial berglas function and configure the mutating webhooks --

PROJECT_ID="warrenburg-portfolio"

# enable cloud functions
gcloud services enable --project "${PROJECT_ID}" \
  cloudfunctions.googleapis.com

# initial function deployment
cd functions || exit
gcloud functions deploy berglas-secrets-webhook \
  --project "${PROJECT_ID}" \
  --allow-unauthenticated \
  --runtime go113 \
  --entry-point F \
  --trigger-http \
  --region us-central1

cd ../

ENDPOINT=$(gcloud functions describe berglas-secrets-webhook --project ${PROJECT_ID} --format 'value(httpsTrigger.url)')

echo "The function endpoint is ${ENDPOINT}"

sed "s|WEBHOOK_URL|$ENDPOINT|" ./kubernetes/webhook.yaml | kubectl apply -f -

kubectl get mutatingwebhookconfiguration
