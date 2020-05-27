(ns authsvc.app
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [bidi.ring :refer (make-handler)]
            [authsvc.middleware :refer :all]))

(defn app [routes]
  (-> routes
      (make-handler)
      wrap-reload
      wrap-json-response
      apply-handlers))
