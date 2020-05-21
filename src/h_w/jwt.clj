(ns h-w.jwt
  (:require [cheshire.core :as json]
            [clojure.string :as string])
  (:import (java.util Base64)))

(defn base64-decode
  "Utility function over the Java 8 base64 decoder"
  [to-decode]
  (String. (.decode (Base64/getDecoder) ^String to-decode)))

(defn string->edn
  "Parse JSON from a string returning an edn map, otherwise nil"
  [string]
  (when-let [edn (json/decode string true)]
    (when (map? edn)
      edn)))

; Using with buddy, so leave the signature undecoded
(defn decoded-jwt
  "Transform a properly formed JWT into a Clojure map"
  [jwt]
  (when-let [jwt-parts (string/split jwt #"\.")]
    (when (= 3 (count jwt-parts))
      (let [[b64-header b64-payload b64-signature] jwt-parts]
        {:header    (string->edn (base64-decode b64-header))
         :payload   (string->edn (base64-decode b64-payload))
         :signature b64-signature}))))

