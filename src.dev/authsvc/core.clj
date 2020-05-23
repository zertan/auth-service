(ns authsvc.core
  (:require [org.httpkit.server :as http]
            [clojure.tools.nrepl.server :as nrepl]
            [authsvc.app :as app]
            [authsvc.config :as config]
            [authsvc.debug :as debug])
  (:gen-class))

(def repl-server (atom nil))
(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))


(defn run-server [app]
  (reset! server (http/run-server app {:port 8080})))

(defn -main [& args]
  (reset! repl-server (nrepl/start-server :port 7890)))
