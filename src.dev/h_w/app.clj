(ns h-w.app
  (:require [h-w.routes :refer [routes]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [h-w.middleware :refer :all]
            [h-w.debug :as debug]))

(defn wrap-to-atom [handler]
  (fn [request] (swap! debug/last-call conj request)
    (handler request)))

(def app
  (->
   #'routes
   wrap-to-atom
   wrap-params
   wrap-body-string
   wrap-cookies
   (wrap-json-body {:keywords? true :bigdecimals? true})
   wrap-json-response
   wrap-reload))
