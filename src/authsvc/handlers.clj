(ns authsvc.handlers
  (:require [cheshire.core :as json]
            [clj-http.lite.client :as client]
            [ring.util.response :refer [redirect]]
            [ring.util.codec :refer [form-decode]]
            [clojure.walk :refer [keywordize-keys]]
            [authsvc.authorization :as authorization]
            [authsvc.jwt :as jwt]
            [authsvc.config :as config]
            [authsvc.debug :as debug]))

(defn token [request]
  (let [body-parsed (keywordize-keys (form-decode (:body-str request)))
        tokens (client/post (str (:sso-url (get @config/config "folksam")) "/protocol/openid-connect/token")
                            {:form-params body-parsed})
        access-token (get (json/parse-string (:body tokens)) "access_token")
        authorized (client/post (str (:sso-url (get @config/config "folksam")) "/protocol/openid-connect/token") {:form-params {:audience (:client_id body-parsed)
                                               :grant_type "urn:ietf:params:oauth:grant-type:uma-ticket" :response_mode "decision"} :headers {"Authorization" (str "Bearer " access-token)}})]
    (swap! debug/auth-state assoc-in [:token] (:body tokens))
    (swap! debug/auth-state assoc-in [:a] authorized)
    (if (= (:body authorized) "{\"result\":true}") ;(authorization/validate-claims [] (jwt/decoded-jwt access-token))
      {:body (:body tokens) :status 200 :headers (:headers tokens)}
      {:body "Unauthorized" :status 401})))

(defn auth [request]
  ;; (swap! debug/auth-state assoc-in [:auth] (:query-params request))
  (redirect (str (:sso-url (get @config/config "folksam")) (:uri request) "?" (:query-string request))))

(defn userinfo [request]
  (let [tkn (get (:headers request) "authorization")
        userinfo (client/get (str (:sso-url (get @config/config "folksam")) "/protocol/openid-connect/userinfo") {:headers {"authorization" tkn}})]
    ;; (swap! debug/auth-state assoc-in [:userinfo] (:body userinfo))
    {:body (:body userinfo) :status 200 :headers (:headers userinfo)}))
