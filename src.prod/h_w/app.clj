(ns h-w.app
  (:require [h-w.routes :refer [routes]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [h-w.middleware :refer :all]))

(def app
  (->
   #'routes
   wrap-params
   wrap-body-string
   wrap-cookies
   (wrap-json-body {:keywords? true :bigdecimals? true})
   wrap-json-response))
