(ns h-w.core
  (:require [ring.adapter.jetty :as jetty]            
            [clojure.tools.nrepl.server :refer [start-server stop-server]]
            [h-w.app :as app])
  (:gen-class))

(def repl-server (atom nil))
(def jetty-server (atom nil))

(defn start-jetty []
  (reset! jetty-server (jetty/run-jetty #'app/app {:port 8080 :join? false})))

(defn -main [& args]
  (reset! repl-server (start-server :port 7890)))
