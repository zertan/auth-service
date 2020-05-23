(ns authsvc.debug)

(def auth-state (atom {:auth nil
                       :home nil
                       :token nil
                       :user-info nil}))

(def last-call (atom nil))

(defn wrap-to-atom [handler]
  (fn [request] (swap! last-call conj request)
    (handler request)))
