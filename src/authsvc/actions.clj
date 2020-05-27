(ns authsvc.actions
  (:require [clj-http.lite.client :as client]
            [authsvc.config :as config]
            [authsvc.jwt :as jwt]
            [clojure.xml :as xml]
            [clojure.zip :as zip]))

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
