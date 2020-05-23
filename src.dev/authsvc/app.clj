(ns authsvc.app
  (:require [authsvc.routes :refer [routes]]
            [ring.util.response :as res]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer  [wrap-json-body wrap-json-response]]
            [authsvc.handlers :refer :all]
            ;[ring.middleware.cookies :refer [wrap-cookies]]
            [bidi.ring :refer (make-handler)]
            [bidi.bidi :as b]
            [authsvc.middleware :refer :all]
            
            [authsvc.debug :as debug]))

(defn wrap-to-atom [handler]
  (fn [request] (swap! debug/last-call conj request)
    (handler request)))

;;(b/match-route routes "/protocol/openid-connect/token")

;;(make-handler routes)

(def routes-2
  ["/protocol/openid-connect/" {"token" token
                                "auth" auth
                                "userinfo" userinfo}])


(defn index-handler
  [request]
  (res/response "Homepage"))

(def handler-2
  (make-handler routes-2))

(def handler
  (make-handler ["/1/2/" {"a" index-handler
                          "b" index-handler}]))

(def test-app
  (->  handler-2
      wrap-reload))

(def app
  (-> handler-2

   wrap-to-atom
   ;;wrap-params
   wrap-body-string
   (wrap-json-body {:keywords? true :bigdecimals? true})
   wrap-json-response
   wrap-reload))
