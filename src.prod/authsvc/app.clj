(ns authsvc.app
  (:require [ring.middleware.json :refer  [wrap-json-response]]
            [bidi.ring :refer (make-handler)]
            [authsvc.middleware :refer :all]))


(defn app [routes]
  (-> routes
      (make-handler)
      wrap-json-response
      apply-handlers))
