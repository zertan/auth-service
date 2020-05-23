(ns authsvc.middleware
  (:require [ring.util.request :refer [body-string]]))

(defn wrap-body-string [handler]
  (fn [request]
    (if (= (type (:body request)) org.httpkit.BytesInputStream)
      (handler (assoc request :body-str (slurp (.bytes (:body request)))))
      (handler request))))
