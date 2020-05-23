(ns authsvc.routes
  (:require [authsvc.handlers :refer :all]
            [ring.util.response :as res]))

;; (defroutes routes
;;   (POST "/protocol/openid-connect/token" request handlers/token)
;;   (GET "/protocol/openid-connect/auth" request handlers/auth)
;;   (GET "/protocol/openid-connect/userinfo" request handlers/userinfo))

(def routes
  ["/protocol/openid-connect/" {"token" (res/response "Homepage")
                                "auth" auth
                                "userinfo" userinfo}])
