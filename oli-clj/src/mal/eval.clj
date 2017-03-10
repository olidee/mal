(ns mal.eval
  (:require [mal.env]))

(declare eval-expr)

(defn eval-ast [ast env]
  (into [] (map #(eval-expr % env)) ast))

(defn- eval-node [n env]
  (cond
    (string? n) (case n
                  ("nil" "true" "false") (read-string n)
                  (mal.env/get-from-env env n))
    (sequential? n) (map #(eval-expr % env) n)
    :else n))

(defn- maldef! [[name value] env]
  (mal.env/set-in-env env name (eval-expr value env)))

(defn- mallet* [[bindings expr] env]
  (if (and (vector? bindings) (even? (count bindings)))
    (let [bindings (apply hash-map bindings)
          new-env (mal.env/create-env env)]
      (doseq [[k v] bindings]
        (mal.env/set-in-env new-env k (eval-expr v new-env)))
      (eval-expr expr new-env))
    (throw (Exception. "Let expression should be of the form (let* [binding expr] (body))"))))

(defn- malfn* [[params body] env]
  (if (vector? params)
    (fn [& args]
      (if (= (count params) (count args))
        (let [fn-env (mal.env/create-env env params args)]
          (eval-expr body fn-env))
        (throw (Exception. "Wrong number of args passed to fn"))))
    (throw (Exception. "Function definition should be of the form (fn* [args] (body))"))))

(defn- malif [[test then else] env]
  (let [body (if (eval-expr test env) then else)]
    (eval-expr body env)))

(defn- maldo [expr env]
  (last (eval-ast expr env)))

(defn- eval-expr [expr env]
  (cond
    (not (seq? expr)) (eval-node expr env)
    (empty? expr) expr
    :else (let [[fn-name & args] expr]
            (case fn-name
              "def!" (maldef! args env)
              "let*" (mallet* args env)
              "fn*"  (malfn* args env)
              "if"   (malif args env)
              "do"   (maldo args env)
              (let [[f & args] (eval-node expr env)]
                (apply f args))))))
