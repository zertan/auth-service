#!/usr/bin/env bash
set -euxo pipefail
export PROJECT_ID=mythic-inn-160013
IMAGE=$1
IMG_MATCH="{{ IMAGE }}"

kubectl delete deployment ccapi || echo "ccapi not deployed"
#kubectl run ccapi --image=${IMAGE} --pod-running-timeout=1m0s # --wait-for --format-template='{{ if .Status.Condition == "Running" }}1{{ else }}0{{ end }}'

cat ./pod.yaml | sed "s|${IMG_MATCH}|${IMAGE}|" | kubectl create -f -

echo "Waiting for container to be ready."
sleep 5

export POD_NAME=$(kubectl get pod -n default --selector=run=ccapi -o jsonpath='{.items...metadata.name}')

kubectl port-forward ${POD_NAME} 7890:7890
