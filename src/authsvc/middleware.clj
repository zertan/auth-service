(ns authsvc.middleware
  (:require [ring.middleware.params :refer [wrap-params]]
            [cheshire.core :as json]
            [ring.middleware.json :refer  [wrap-json-body]]))

(defn wrap-client-id [handler]
  (fn [request]
    (if (and (contains? (:params request) "client_id")
             (contains? request :context))
      (swap! (:context request) conj {:client-id (get (:params request) "client_id")}))
    (handler request)))

(defn wrap-token [handler]
  (fn [request]
    (if (= (type (:body request)) clojure.lang.PersistentHashMap)
      (if (contains? (:body request) "access_token")
        (if (contains? request :context)
          (do (swap! (:context request) conj {:access-token (get (:body request) "access_token")})
              (handler request))
          (handler (assoc request :access-token (get (:body request) "access_token")))))
      (handler request))))

(defn apply-handlers [handler]
  (-> handler
      wrap-client-id
      wrap-token
      wrap-params
      (wrap-json-body handler {:keywords? true :bigdecimals? true})))

(defn cond-json [handler]
  (fn [request]
    (if (= (type (:body request)) java.lang.String)
       (handler (assoc request :body (json/parse-string (:body request))))
      (if-not (= (type (:body request)) clojure.lang.PersistentHashMap)
        ((wrap-json-body handler {:keywords? true :bigdecimals? true}) request)
        (handler request)))))
