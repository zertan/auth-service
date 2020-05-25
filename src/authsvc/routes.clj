(ns authsvc.routes
  (:require [authsvc.handlers :refer :all]
            [authsvc.config :as config]
            [ring.util.response :as response]))

(def routes
  ["/protocol/openid-connect/" {"token" token
                                "auth" (fn [request]
                                         (response/redirect (str (-> @config/config :sso :url) (:uri request) "?" (:query-string request))))
                                "userinfo" forward}])
