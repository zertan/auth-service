(ns authsvc.handlers
  (:require [clj-http.lite.client :as client]
            [ring.util.response :as response]
            [authsvc.authorization :as auth]
            [authsvc.actions :as actions]
            [authsvc.debug :as debug]
            [authsvc.middleware :as middleware]
            [authsvc.config :as config]))

(defn wrapped-req [request]
  ((middleware/apply-handlers #(%)) (client/request (merge request {:trow-errors false}))))

(defn redirect [request]
  (response/redirect (str (:forward-url request) "?" (:query-string request))))

(defn forward [request & forward-host]
  (let [forward-url (or forward-host (:forward-url request))
        s (merge request {:method (:request-method request)
                          :url (forward-url)})
        a (swap! debug/last-call conj s)
        r (client/request s)]
    (swap! debug/last-call conj r)
    r))

(defn forward [request]
  (client/request (merge request
                         {:url (-> @config/config :sso :url) "?" (:query-string request)})))

;; (defn token [request]
;;   (if (:decision request)
;;     (forward (auth/ ( )) )
;;     (do (client/request (merge request {:url } auth/decision-request))
;;         (forward 
;;          (merge {:url (str "http://localhost:8080/protocol/openid-connect/token")}))))
;;   (client/request (assoc request :url (str "http://localhost:8080" (:uri request)))))

(defn token [request]
  (let [ctx (atom {:url (-> @config/config :sso :url)})
        a (wrapped-req (merge request @ctx {:url (str (-> @config/config :sso :url) (:uri request))
                                            :context ctx}))
        b (wrapped-req (merge (auth/decision-request @ctx) {:url (str (-> @config/config :sso :url) "/protocol/openid-connect/token")}))]
    a))
