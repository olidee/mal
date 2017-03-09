(ns mal.reader
  (:require [mal.util :as util]))

(def regexp #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:\\.|[^\\\"])*\"|;.*|[^\s\[\]{}('\"`,;)]*)")

(defn tokenizer [str]
  (into [] (comp (map #(second %))
                 (filter #(not (empty? %))))
        (re-seq regexp str)))

(declare read-form)

(defn read-list
  ([tokens]
   (read-list [] tokens))
  ([ast [t & ts :as tokens]]
   (case t
     ")" [ast ts]
     (let [[_ast _ts] (read-form tokens)]
       (recur (conj ast _ast) _ts)))))

(defn read-atom [[t & ts]]
  [(util/parse t) (vec ts)])

(defn read-form [[t & ts :as tokens]]
  (case t
    nil (throw (Exception. "Mismatched parens"))
    "(" (read-list ts)
    (read-atom tokens)))

(defn read-expr [str]
  (loop [expr [] tokens (tokenizer str)]
    (if (empty? tokens)
      expr
      (let [[_expr tokens] (read-form tokens)]
        (recur (conj expr _expr) tokens)))))
