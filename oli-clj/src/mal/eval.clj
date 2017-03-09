(ns mal.eval
  (:require [mal.env]))

(declare eval-expr)

(defn eval-ast [ast env]
  (into [] (map #(eval-expr % env)) ast))

(defn- eval-node [n env]
  (cond
    (string? n) (mal.env/get-from-env (mal.env/find-env-for env n) n)
    (vector? n) (map #(eval-expr % env) n)
    :else n))

(defn- maldef! [name value env]
  (mal.env/set-in-env env name (eval-node value env)))

(defn- mallet* [bindings expr env]
  (let [bindings (apply hash-map bindings)
        new-env (mal.env/create-env env)]
    (doseq [[k v] bindings]
      (mal.env/set-in-env new-env k (eval-node v new-env)))
    (eval-expr expr new-env)))

(defn- eval-expr [expr env]
  (cond
    (not (vector? expr)) (eval-node expr env)
    (empty? expr) expr
    :else (let [[fn-name arg1 arg2] expr]
            (case fn-name
              "def!" (maldef! arg1 arg2 env)
              "let*" (mallet* arg1 arg2 env)
              (let [[f & args] (eval-node expr env)]
                (apply f args))))))
