(ns h-w.handlers
  (:require [cheshire.core :as json]
            [clj-http.client :as client]
            [ring.util.response :refer [redirect]]
            [ring.util.codec :refer [form-decode]]
            [clojure.walk :refer [keywordize-keys]]
            [h-w.authorization :as authorization]
            [h-w.jwt :as jwt]
            [h-w.configuration :as config]
            [h-w.constants :refer :all]))

(defn token [request]
  (let [tokens (client/post (str (:sso-url @config/config) token-endpoint)
                            {:form-params (keywordize-keys (form-decode (:body-str request)))})
        access-token (jwt/decoded-jwt (get (json/parse-string (:body tokens)) "access_token"))]
    ;; (swap! debug/auth-state assoc-in [:token] (:body tokens))
    (if (authorization/validate-claims access-token)
      {:body (:body tokens) :status 200 :headers (:headers tokens)}
      {:body "Unauthorized" :status 401})))

(defn auth [request]
  ;; (swap! debug/auth-state assoc-in [:auth] (:query-params request))
  (redirect (str (:sso-url @config/config) (:uri request) "?" (:query-string request))))

(defn userinfo [request]
  (let [tkn (get (:headers request) "authorization")
        userinfo (client/get (str (:sso-url @config/config) userinfo-endpoint) {:headers {"authorization" tkn}})]
    ;; (swap! debug/auth-state assoc-in [:userinfo] (:body userinfo))
    {:body (:body userinfo) :status 200 :headers (:headers userinfo)}))
