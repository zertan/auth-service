(ns authsvc.middleware
  (:require [ring.util.request :refer [body-string]]
            [clojure.walk :refer [keywordize-keys]]
            [ring.util.codec :refer [form-decode]]
            [authsvc.config :as config]))

(defn wrap-body-string [handler]
  (fn [request]
    (if (= (type (:body request)) org.httpkit.BytesInputStream)
      (handler (assoc request :body (slurp (.bytes (:body request)))))
      (handler request))))

(defn wrap-body-form-parsed [handler]
  (fn [request]
    (handler (assoc request :body-parsed (keywordize-keys (form-decode (:body request)))))))

(defn wrap-forward-url [handler]
  (fn [request]
    (handler (assoc request :forward-url (str (:sso-url @config/config) (:uri request))))))

(defn wrap-token [handler]
  (fn [request]
    (if-let [access-token (get (:body request) "access_token")]
      (handler (assoc request :access-token access-token))
      (handler request))))
