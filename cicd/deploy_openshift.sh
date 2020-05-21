#!/usr/bin/env bash
#oc new-project ccapi
oc import-image mpiech/s2i-clojure --confirm
#oc import-image my-redhat-openjdk-18/openjdk18-openshift --from=registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift --confirm
lein uberjar
cp target/uberjar/h-w-0.1.0-SNAPSHOT-standalone.jar deployments
#   ;;oc new-build mpiech/s2i-clojure~./deployments --name=ccapi
#oc new-build --binary=true --name=h-w -i=redhat-openjdk18-openshift:1.2
oc start-build --from-dir deployments/ h-w

# starting the app
#oc new-app h-w:latest --image-stream="tieto-poc-3scale-2/h-w" --allow-missing-imagestream-tags

# expose
#oc expose svc/h-w --port 7890
#oc expose svc/h-w --port 8080
#oc get pods
#oc port-forward ccapi-3-ln7ea 7890:7890
