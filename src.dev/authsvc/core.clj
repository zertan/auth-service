(ns authsvc.core
  (:require [org.httpkit.server :as http]
            [clojure.tools.nrepl.server :as nrepl]
            [authsvc.config :as config]
            [authsvc.app :as app])
  (:gen-class))

(defonce repl-server (atom nil))
(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn run-server [app]
  (reset! server (http/run-server app {:port 8081})))

(defn -main [& args]
  (config/load-config)
  (reset! repl-server (nrepl/start-server :port 7890)))

;;(run-server app/app)
