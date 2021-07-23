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
  (let [h (conj (:headers request) {"x-forwarded-for" (:remote-addr request)
                                    ;"x-forwarded-host" (:remote-addr request)
                                    "host" forward-url})
        r (-> request
              (assoc-in [:headers] h)
              (assoc :server-name forward-url)
              (assoc :url (str forward-url (:uri request))))]
    (println "forwarding request: " r)
    ;(client/request r)
    ))

(defn token [request]
  (println "sending request to token endpoint: " request)
  (let [ctx (atom {:url (-> @config/config :sso :url)})
        cr ((middleware/wrap-client-id (fn [x] x)) (merge request {:context ctx}))
        a (client/request (merge request {:url (str (-> @config/config :sso :url) (:uri request))
                                          :context ctx}))
        b ((middleware/cond-json (middleware/wrap-token (fn [x] x))) (assoc a :context ctx))
        c (client/request (merge (auth/decision-request @ctx) {:url (str (-> @config/config :sso :url) "/protocol/openid-connect/token")}))]
    (go (<! (timeout 500))
        (actions/set-section-access @ctx))
    (println "got token response: " a)
    (println "got decision request: " c)
    a))
