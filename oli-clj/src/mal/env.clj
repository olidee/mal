(ns mal.env)

(defn- find-env-for [env k]
  (if (contains? (:env @env) k)
    env
    (if-let [outer (:outer @env)]
      (find-env-for outer k)
      (throw (Exception. (str "Symbol '" k "' not found"))))))

(defn create-env
  ([outer] (atom {:env {} :outer outer}))
  ([outer binds exprs]
   (let [env (zipmap binds exprs)]
     (atom {:env env :outer outer}))))

(defn set-in-env [env k v]
  (swap! env assoc-in [:env k] v)
  v)

(defn get-from-env [env k]
  (get-in @(find-env-for env k) [:env k]))
