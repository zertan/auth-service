(ns authsvc.app
  (:require [authsvc.routes :refer [routes]]
            [ring.middleware.params :refer [wrap-params]]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            ;;[ring.middleware.cookies :refer [wrap-cookies]]
            [authsvc.middleware :refer :all]))

(def app
  (->
   routes
   (make-handler)
   wrap-params
   wrap-body-string
   ;;wrap-cookies
   (wrap-json-body {:keywords? true :bigdecimals? true})
   wrap-json-response
   ))
