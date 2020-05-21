(ns h-w.routes
  (:require [compojure.core :refer [GET POST defroutes]]
            [h-w.handlers :as handlers]))

(defroutes routes
  (POST "/protocol/openid-connect/token" request handlers/token)
  (GET "/protocol/openid-connect/auth" request handlers/auth)
  (GET "/protocol/openid-connect/userinfo" request handlers/userinfo))
