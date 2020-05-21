(ns h-w.middleware
  (:require [ring.util.request :refer [body-string]]))

(defn wrap-body-string [handler]
  (fn [request]
    (let [body-str (body-string request)]
      (handler (assoc request :body-str (slurp (java.io.StringReader. body-str)))))))
