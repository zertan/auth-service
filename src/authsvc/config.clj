(ns authsvc.config
  (:require [environ.core :refer [env]]))

(defn config-file []
  (or (env :authsvc-config-file) "auth-config.edn"))

(def config (atom nil))

(defn load-config []
  (reset! config (read-string (slurp (config-file)))))
