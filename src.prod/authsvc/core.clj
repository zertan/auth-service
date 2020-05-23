(ns authsvc.core
  (:require [org.httpkit.server :as http]
            [authsvc.app :as app])
  (:gen-class))

(defn -main [& args]
  (http/run-server #'app/app {:port 8080}))
