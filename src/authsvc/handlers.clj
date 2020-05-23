(ns authsvc.handlers
  (:require [clj-http.lite.client :as client]
            [ring.util.response :as response]
            [authsvc.authorization :as auth]
            [authsvc.actions :as actions]))

(defn redirect [request]
  (response/redirect (str (:forward-url request) "?" (:query-string request))))

(defn forward [request & [opts]]
  (client/request (merge-with merge {:method (:request-method request)
                                     :url (:forward-url request)}
                              opts)))

(defn token [request]
  (if (= (:server-name request)
         "localhost")
    (if (contains? (:form-params request) :audience)
        (auth/verify-authorization (forward request))
        (forward request))
    (let [init (client/request (assoc request :url (str "http://localhost:8080/" (:uri request))))
          auth (client/request (merge request {:url (str "http://localhost:8080/")} auth/decision-request))]
      init)))
