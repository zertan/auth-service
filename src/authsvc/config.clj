(ns authsvc.config
  (:require [environ.core :refer [env]]))

(def config (atom nil))

(defn load-config []
  (reset! config (read-string (slurp (env :authsvc-config-file)))))
