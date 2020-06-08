# Auth service

This is a service that can be put in-between a 3Scale API Management installation and a RH SSO/Keycloak instance. It intercepts the OpenID connect flow and performs extra authorization checks (based on role claims in the issued token) and calls the 3Scale API to set the correct section accesses based on the defined roles.

## Building the image

Prerequisites: oc command line utility with connection to an OpenShift cluster.

Create the Clojure builder:

```bash
oc project *namespace-in-which-to-deploy* # put it in project 'default' to use in all namespaces
oc new-build https://github.com/tfriman/s2i-clojure#v1.0.0 --name s2i-clojure
```

Build the auth service with the Clojure builder:

```bash
oc new-build s2i-clojure~https://github.com/zertan/auth-service.git --name=authsvc
```

## Installation

First build the image by following the instructions in the previous section.

Update the information in the supplied configmap template (see usage below):

```bash
vi openshift/cm.yml
oc create -f openshift/cm.yml
```

Then deploy the service:

```bash
oc new-app -f openshift/authsvc.yml --name authsvc \
           -p WILDCARD_DOMAIN=apps.your.domain.com
```

## Usage

Configure the SSO integration as usual in 3Scale and RH SSO with the usual clients etc. BUT exchange the realm URL endpoint with the auth service route published during deployment.

The configmap template is shown below and should be self-explanatory:

```clojure
{:sso {:url "sso-url"}
 :threescale {:url "threescale-url"
              :api-access-token "threescale provider access token"
              :role-section {"name of realm role in SSO that should be able to access the admin console and sections" ["portal" "monitoring" "plans" "partner"]}}}
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
