(ns authsvc.handlers
  (:require [cheshire.core :as json]
            [clj-http.lite.client :as client]
            [ring.util.response :refer [redirect]]
            [ring.util.codec :refer [form-decode]]
            [clojure.walk :refer [keywordize-keys]]
            [authsvc.config :as config]))

(defn token [request]
  (let [body-parsed (keywordize-keys (form-decode (:body-str request)))
        tokens (client/post (str (:sso-url @config/config) "/protocol/openid-connect/token")
                            {:form-params body-parsed})
        access-token (get (json/parse-string (:body tokens)) "access_token")
        authorized (client/post (str (:sso-url @config/config) "/protocol/openid-connect/token") {:form-params {:audience (:client_id body-parsed)
                                               :grant_type "urn:ietf:params:oauth:grant-type:uma-ticket" :response_mode "decision"} :headers {"Authorization" (str "Bearer " access-token)}})]
    (if (= (:body authorized) "{\"result\":true}")
      {:body (:body tokens) :status 200 :headers (:headers tokens)}
      {:body "Unauthorized" :status 401})))

(defn auth [request]
  (redirect (str (:sso-url @config/config) (:uri request) "?" (:query-string request))))

(defn userinfo [request]
  (let [tkn (get (:headers request) "authorization")
        userinfo (client/get (str (:sso-url @config/config) "/protocol/openid-connect/userinfo") {:headers {"authorization" tkn}})]
    {:body (:body userinfo) :status 200 :headers (:headers userinfo)}))
