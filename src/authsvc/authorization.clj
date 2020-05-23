(ns authsvc.authorization)

(def decision-request
  {:form-params {:audience (:client_id (:body-parsed 'request))
                 :grant_type "urn:ietf:params:oauth:grant-type:uma-ticket"
                 :response_mode "decision"}
   :headers {"Authorization" (str "Bearer " (:access-token 'request))}})

(defn verify-authorization [response]
  (if (= (:body response) "{\"result\":true}")
    response
    {:body "Unauthorized" :status 401}))
