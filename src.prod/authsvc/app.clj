(ns authsvc.app
  (:require [authsvc.routes :refer [routes]]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            [authsvc.middleware :refer :all]))

(def app
  (-> routes
      (make-handler)
      wrap-body-string
      (wrap-json-body {:keywords? true :bigdecimals? true})
      wrap-json-response))
