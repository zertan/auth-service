(ns authsvc.config
  (:require [environ.core :refer [env]]))

;{:sso-url "https://tieto-idp.apps.openshift.onecloud.tieto.com/auth/realms/folksam"}

(def config (atom nil))

(defn load-config []
  (reset! config (read-string (slurp (env :authsvc-config-file)))))
