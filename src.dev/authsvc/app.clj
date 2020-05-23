(ns authsvc.app
  (:require [authsvc.routes :refer [routes]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            [authsvc.handlers :refer :all]
            [bidi.ring :refer (make-handler)]
            [authsvc.middleware :refer :all]
            [authsvc.debug :as debug]))

(def app
  (-> routes
      (make-handler)
      wrap-body-string
      wrap-params
      (wrap-json-body {:keywords? true :bigdecimals? true})
      wrap-json-response
      wrap-forward-url
      wrap-token
      wrap-reload
      debug/wrap-to-atom))
