(ns authsvc.app
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [bidi.ring :refer (make-handler)]
            [authsvc.middleware :refer :all]))

(defn app [routes]
  (-> routes
      (make-handler)
      apply-handlers
      wrap-json-response
      wrap-reload))

