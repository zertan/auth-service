(ns authsvc.config
  (:require [environ.core :refer [env]]))

(def config (atom nil))

(defn load []
  (reset! config (read-string (slurp (env :authsvc-config-file)))))

(defn forward-url [request]
  (str (:sso-url @config) (:uri request)))
