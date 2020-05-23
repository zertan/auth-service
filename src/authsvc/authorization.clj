(ns authsvc.authorization
  (:require [authsvc.jwt :as jwt]
            [cheshire.core :as json]
            [authsvc.configuration :as config]))

(defn validate-claims [fns token]
  (every? true? (map #(% token) fns)))
;;   (some #(= % "3Scale Service Developer/API definer")
;;         (-> token :payload :realm_access :roles)))
