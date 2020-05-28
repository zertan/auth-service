(ns authsvc.core
  (:require [org.httpkit.server :as http]
            [authsvc.routes :refer [routes]]
            [authsvc.config :as config]
            [authsvc.app :as app])
  (:gen-class))

(defn -main [& args]
  (config/load-config)
  (http/run-server (app/app (routes)) {:port 8080}))
