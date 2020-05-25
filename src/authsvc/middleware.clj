(ns authsvc.middleware
  (:require [ring.util.request :refer [body-string]]
            [clojure.walk :refer [keywordize-keys]]
            [ring.util.codec :refer [form-decode]]
            [authsvc.config :as config]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer  [wrap-json-body]]
            [authsvc.debug :as debug]))

(defn wrap-body-string [handler]
  (fn [request]
    (if (= (type (:body request)) org.httpkit.BytesInputStream)
      (handler (assoc request :body (slurp (.bytes (:body request)))))
      (handler request))))

(defn wrap-body-form-parsed [handler]
  (fn [request]
    (if (= (type (:body request)) java.lang.String)
        (handler (assoc request :body-parsed (keywordize-keys (form-decode (:body request)))))
        (handler request))))

(defn wrap-forward-url [handler]
  (fn [request]
    (handler (assoc request :forward-url (str (:sso-url @config/config) (:uri request))))))

(defn wrap-client-id [handler]
  (fn [request]
    (if (and (contains? (:params request) "client_id")
             (contains? request :context))
      (swap! (:context request) conj {:client-id (get (:params request) "client_id")}))))

(defn wrap-token [handler]
  (fn [request]
    (if (= (type (:body request)) clojure.lang.PersistentHashMap)
      (if (contains? (:body request) :access_token)
        (if (contains? request :context)
          (swap! (:context request) conj {:access-token (:access_token (:body request))})
          (handler (assoc request :access-token (:access_token (:body request))))))
      (handler request))))

(defn cond-json [handler]
  (fn [request]
    (if-not (= (type (:body request)) clojure.lang.PersistentHashMap)
      ((wrap-json-body handler {:keywords? true :bigdecimals? true}) request)
      (handler request))))

(defn apply-handlers [handler]
  (-> handler
      debug/wrap-to-atom
      wrap-body-string
      cond-json
      wrap-params
      wrap-body-form-parsed
      wrap-token))
