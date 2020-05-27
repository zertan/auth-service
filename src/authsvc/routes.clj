(ns authsvc.routes
  (:require [authsvc.handlers :refer :all]
            [authsvc.config :as config]
            [ring.util.response :as response]))

(def routes
  (let [forward-url (-> @config/config :sso :url)]
    ["/protocol/openid-connect/" {"token" #'token
                                  "auth" (fn [request]
                                           (response/redirect (str forward-url (:uri request) "?" (:query-string request))))
                                  "userinfo" (fn [request] (#'forward request forward-url))}]))
