(ns authsvc.routes
  (:require [authsvc.handlers :refer :all]
            [clj-http.lite.client :as client]
            [authsvc.config :as config]
            [ring.util.response :as response]))

(defn routes []
  (let [forward-url (-> @config/config :sso :url)]
    ["/protocol/openid-connect/" {"token" token
                                  "auth" (fn [request]
                                           (response/redirect (str forward-url (:uri request) "?" (:query-string request))))
                                  "userinfo" (fn [request]
                                               (println "req from 3scale: " request)
                                               (let [token (get (:headers request) "authorization")
                                                     request2 {:url (str (-> @config/config :sso :url) "/protocol/openid-connect/userinfo")
                                                               :method :get
                                                               :headers {"Authorization" token}}]
                                                 (println "forwarding request: " request2)
                                                 (client/request request2)
                                                 )) ;(fn [request] (forward request forward-url))
                                  }]))
