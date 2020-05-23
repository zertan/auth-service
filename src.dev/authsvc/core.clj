(ns authsvc.core
  (:require [org.httpkit.server :as http]
            [clojure.tools.nrepl.server :as nrepl]
            [authsvc.app :as app])
  (:gen-class))

(defonce repl-server (atom nil))
(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn run-server [app]
  (reset! server (http/run-server app {:port 8080})))

(defn -main [& args]
  (reset! repl-server (nrepl/start-server :port 7890)))
