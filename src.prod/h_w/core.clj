(ns h-w.core
  (:require [ring.adapter.jetty :as jetty]
            [h-w.app :as app])
  (:gen-class))

(def jetty-server (atom nil))

(defn start-jetty []
  (reset! jetty-server (jetty/run-jetty #'app/app {:port 8080 :join? false})))

(defn -main [& args]
  (start-jetty))
