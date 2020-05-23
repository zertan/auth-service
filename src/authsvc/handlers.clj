(ns authsvc.handlers
  (:require [cheshire.core :as json]
            [clj-http.lite.client :as client]
            [ring.util.response :refer [redirect]]
            [ring.util.codec :refer [form-decode]]
            [clojure.walk :refer [keywordize-keys]]
            [authsvc.config :as config :refer [forward-url]]
            [authsvc.authorization :as auth]
            [authsvc.actions :as actions]))

(defn token [request]
  (let [body-parsed (keywordize-keys (form-decode (:body-str request)))
        tokens (client/post (forward-url request)
                            {:form-params body-parsed})
        access-token (get (json/parse-string (:body tokens)) "access_token")
        authorized (client/post (forward-url request)
                                {:form-params {:audience (:client_id body-parsed)
                                               :grant_type "urn:ietf:params:oauth:grant-type:uma-ticket"
                                               :response_mode "decision"}
                                 :headers {"Authorization" (str "Bearer " access-token)}})]
    (if (= (:body authorized) "{\"result\":true}")
      tokens
      {:body "Unauthorized" :status 401})))

(defn auth [request]
  (redirect (str (forward-url request) "?" (:query-string request))))

(defn userinfo [request]
  (client/get (forward-url request) request))
