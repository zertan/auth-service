(ns h-w.authorization
  (:require [h-w.jwt :as jwt]
            [cheshire.core :as json]
            [h-w.configuration :as config]))

(defn validate-claims [token]
  (some #(= % "3Scale Service Developer/API definer")
        (-> token :payload :realm_access :roles)))
