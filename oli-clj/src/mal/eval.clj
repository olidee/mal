(ns mal.eval)

(declare eval-expr)

(defn- eval-symbol [s env]
  (if-let [[k v] (find env s)]
    v
    (throw (Exception. (str "Symbol '" s "' not found")))))

(defn- eval-node [n env]
  (cond
    (string? n) (eval-symbol n env)
    (vector? n) (map #(eval-expr % env) n)
    :else n))

(defn- eval-expr [expr env]
  (cond
    (not (vector? expr)) (eval-node expr env)
    (empty? expr) expr
    :else (let [[f & args] (eval-node expr env)]
            (apply f args))))

(defn eval-ast [ast env]
  (into [] (map #(eval-expr % env)) ast))
