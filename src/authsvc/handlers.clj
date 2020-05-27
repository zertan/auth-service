(ns authsvc.handlers
  (:require [clj-http.lite.client :as client]
            [ring.util.response :as response]
            [authsvc.authorization :as auth]
            [authsvc.actions :as actions]
            [authsvc.debug :as debug]
            [authsvc.middleware :as middleware]
            [authsvc.config :as config]
            [authsvc.jwt :as jwt]
            [clojure.xml :as xml]
            [clojure.zip :as zip]))

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
    a))

(defn get-sections [roles]
  (distinct (mapcat #(get (-> @config/config :threescale :role-section) %) roles)))

(defn usermap [context]
  (let [token (jwt/decode-jwt (:access-token context))]
    {:username (:preferred_username token)
     :roles (:roles (:realm_access token))}))

(defn encode-body-str [access-token sections]
  (str "access_token=" access-token (reduce str (map (fn [x] (str "&allowed_sections%5B%5D=" x)) sections))))

(defn zip-str [s]
  (zip/xml-zip 
      (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(defn user-map [response]
  (into {} (map (fn [x] {(first (:content (nth (-> x :content) 6)))
                         (first (-> x :content first :content))})
                (:content (first (zip-str (:body response)))))))

(defn find-user-id [username]
  (let [url (-> @config/config :threescale :url)
        r (client/request (merge {:query-params {:access_token (-> @config/config :threescale :api-access-token)}}
                                 {:method :get
                                  :url (str url "/admin/api/users.xml")}))
        m (user-map r)]
    (get m username)))

(defn set-section-access [context]
  (let [u (usermap context)
        sections (get-sections (:roles u))
        id (find-user-id (:username u))]
    (client/put (str (-> @config/config :threescale :url) "/admin/api/users/" id "/permissions.xml")
                {:body (encode-body-str (-> @config/config :threescale :api-access-token) sections)
                 :content-type "application/x-www-form-urlencoded"})))
