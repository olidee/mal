(ns mal.env)

(defn create-env [outer]
  (atom {:env {} :outer outer}))

(defn set-in-env [env k v]
  (swap! env assoc-in [:env k] v)
  v)

(defn get-from-env [env k]
  (get-in @env ["env" k]))

(defn find-env-for [env k]
  (if (contains? (:env @env) k)
    env
    (if-let [outer (:outer @env)]
      (find-env-for outer k)
      (throw (Exception. (str "Symbol '" k "' not found"))))))
