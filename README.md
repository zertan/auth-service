# Auth service

This is a service that can be put in-between a 3Scale API Management installation and a RH SSO/Keycloak instance. It intercepts the OpenID connect flow and performs extra authorization checks (based on role claims in the issued token) and calls the 3Scale API to set the correct section accesses based on the defined roles.

## Building the jar and image

Prerequisites: oc command line utility with connection to an OpenShift cluster.

```bash
oc project *namespace-to-deploy-in*
cicd/build.sh
``` 

Run the openshift build script

## Installation

First build the image by following the instructions in the previous section.

Update the information in the supplied configmap template and deploy:

```bash
vi openshift/cm.yml
oc create -f openshift/cm.yml
```

Then deploy the service:

```bash
oc new-app -f openshift/authsvc.yml
```

## License

Copyright © 2020 Daniel Hermansson

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
