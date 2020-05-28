(ns authsvc.handlers
  (:require [clojure.core.async :refer [go <! timeout]]
            [clj-http.lite.client :as client]
            [ring.util.response :as response]
            [authsvc.authorization :as auth]
            [authsvc.middleware :as middleware]
            [authsvc.config :as config]
            [authsvc.actions :as actions]))

(defn wrapped-req [request]
  ((middleware/apply-handlers #(%)) (client/request request)))

(defn redirect [request]
  (response/redirect (str (:forward-url request) "?" (:query-string request))))

(defn forward [request forward-url]
  (client/request (merge request
                         {:url (str forward-url (:uri request))})))

(defn token [request]
  (let [ctx (atom {:url (-> @config/config :sso :url)})
        cr ((middleware/wrap-client-id (fn [x] x)) (merge request {:context ctx}))
        a (client/request (merge request {:url (str (-> @config/config :sso :url) (:uri request))
                                          :context ctx}))
        b ((middleware/cond-json (middleware/wrap-token (fn [x] x))) (assoc a :context ctx))
        c (client/request (merge (auth/decision-request @ctx) {:url (str (-> @config/config :sso :url) "/protocol/openid-connect/token")}))]
    (go (<! (timeout 500))
        (actions/set-section-access @ctx))
    a))
