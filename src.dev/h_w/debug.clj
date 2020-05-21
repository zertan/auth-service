(ns h-w.debug
  (:gen-class))

(def auth-state (atom {:auth nil
                       :home nil
                       :token nil
                       :user-info nil}))

(def last-call (atom nil))
