(ns mal.eval
  (:require [mal.env]))

(declare eval-expr)

(defn eval-ast [ast env]
  (into [] (map #(eval-expr % env)) ast))

(defn- eval-node [n env]
  (cond
    (string? n) (mal.env/get-from-env env n)
    (vector? n) (map #(eval-expr % env) n)
    :else n))

(defn- maldef! [name value env]
  (mal.env/set-in-env env name (eval-expr value env)))

(defn- mallet* [bindings expr env]
  (let [bindings (apply hash-map bindings)
        new-env (mal.env/create-env env)]
    (doseq [[k v] bindings]
      (mal.env/set-in-env new-env k (eval-expr v new-env)))
    (eval-expr expr new-env)))

(defn- malfn* [params body env]
  (if (vector? params)
    (fn [& args]
      (if (= (count params) (count args))
        (let [fn-env (mal.env/create-env env params args)]
          (eval-expr body fn-env))
        (throw (Exception. "Wrong number of args passed to fn"))))
    (throw (Exception. "Function definition should be of the form (fn* (args) (body))"))))

(defn- malif [test then else env]
  (let [body (if (eval-expr test env) then else)]
    (eval-expr body env)))

(defn- maldo [expr env]
  (last (eval-ast expr env)))

(defn- eval-expr [expr env]
  (cond
    (not (vector? expr)) (eval-node expr env)
    (empty? expr) expr
    :else (let [[fn-name arg1 arg2 arg3] expr]
            (case fn-name
              "def!" (maldef! arg1 arg2 env)
              "let*" (mallet* arg1 arg2 env)
              "fn*"  (malfn* arg1 arg2 env)
              "if"   (malif arg1 arg2 arg3 env)
              "do"   (maldo arg1 env)
              (let [[f & args] (eval-node expr env)]
                (apply f args))))))
