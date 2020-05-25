(ns authsvc.authorization)

(defn decision-request [context]
  {:uri "/protocol/openid-connect/token"
   :form-params {:audience (:client-id context)
                 :grant_type "urn:ietf:params:oauth:grant-type:uma-ticket"
                 :response_mode "decision"}
   :headers {"Authorization" (str "Bearer " (:access-token context))}})

(defn verify-authorization [response]
  (if (= (:body response) "{\"result\":true}")
    response
    {:body "Unauthorized" :status 401}))
