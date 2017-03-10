(ns mal.reader
  (:require [mal.util :as util]))

(def regexp #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:\\.|[^\\\"])*\"|;.*|[^\s\[\]{}('\"`,;)]*)")

(def seq-types
  [{:tokens "()" :fn #(reverse %) :type :list}
   {:tokens "[]" :fn identity :type :vector}])

(defn- seq-token? [f t]
  (= t (map #(f (:tokens %)) seq-types)))

(defn- seq-start? [t] (seq-token? first t))
(defn- seq-end? [t] (seq-token? second t))

(defn- get-seq [t]
  (let [seq-type (filter #(= t (first (:tokens %))) seq-types)]
    [(read-string (:tokens seq-type)) (:fn seq-type)]))

(defn- tokenizer [str]
  (into [] (comp (map #(second %))
                 (filter #(not (empty? %))))
        (re-seq regexp str)))

(declare read-form)
(defn read-seq
  ([tokens]
   (read-seq (get-seq (first tokens)) tokens))
  ([[ast f] [t & ts :as tokens]]
   (if (seq-end? t)
     [(f ast) ts]
     (let [[_ast _ts] (read-form tokens)]
       (recur (conj ast _ast) _ts)))))

(defn read-atom [[t & ts]]
  [(util/parse t) (vec ts)])

(defn read-form [[t & ts :as tokens]]
  (cond
    (= t nil) (throw (Exception. "Mismatched parens"))
    (seq-start? t) (read-seq tokens)
    :else (read-atom tokens)))

(defn read-expr [str]
  (loop [expr [] tokens (tokenizer str)]
    (if (empty? tokens)
      expr
      (let [[_expr tokens] (read-form tokens)]
        (recur (conj expr _expr) tokens)))))
