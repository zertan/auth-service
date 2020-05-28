(ns authsvc.app
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [bidi.ring :refer (make-handler)]
            [authsvc.middleware :refer :all]
            [authsvc.debug :as debug]))

(defn app [routes]
  (-> routes
      (make-handler)
      wrap-reload
      wrap-json-response
      debug/wrap-to-atom
      apply-handlers))
