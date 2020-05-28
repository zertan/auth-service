(ns authsvc.authorization)

(defn decision-request [context]
  {:uri "/protocol/openid-connect/token"
   :method :post
   :form-params {:audience (:client-id context)
                 :grant_type "urn:ietf:params:oauth:grant-type:uma-ticket"
                 :response_mode "decision"}
   :headers {"Authorization" (str "Bearer " (:access-token context))}})
