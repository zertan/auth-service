#!/usr/bin/env bash
set -euxo pipefail

TOP_DIR=$(git rev-parse --show-toplevel)
PROJECT_ID=some-proj-277421
VERSION=0.0.1
RELEASE=$(set +euxo pipefail && tr -dc 'a-z0-9' < /dev/urandom | dd bs=8 count=1 status=none && set -euxo pipefail)
IMAGE_NAME=gcr.io/${PROJECT_ID}/h-w:${VERSION}-${RELEASE}

cd ${TOP_DIR}

lein uberjar

rm ${TOP_DIR}/docker/h-w-0.1.0-SNAPSHOT-standalone.jar || echo "Jar not present in ./docker"

cp -f ${TOP_DIR}/target/uberjar/h-w-0.1.0-SNAPSHOT-standalone.jar ${TOP_DIR}/docker

cd ${TOP_DIR}/docker && docker build -t ${IMAGE_NAME} .

gcloud docker -- push ${IMAGE_NAME}

echo "${IMAGE_NAME}"
