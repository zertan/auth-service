(ns authsvc.routes
  (:require [authsvc.handlers :refer :all]))

(def routes
  ["/protocol/openid-connect/" {"token" token
                                "auth" auth
                                "userinfo" userinfo}])
