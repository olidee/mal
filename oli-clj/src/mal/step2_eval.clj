(ns mal.step2-eval
  (:require [mal.reader :as reader]
            [mal.eval :as eval]
            [mal.printer :as printer]
            [mal.util :as util]))

(def env {"+" (fn [x y] (+ x y))
          "-" (fn [x y] (- x y))
          "*" (fn [x y] (* x y))
          "/" (fn [x y] (/ x y))})

(defn READ [str]
  (reader/read-expr str))

(defn EVAL [ast env]
  (eval/eval-ast ast env))

(defn PRINT [expr]
  (printer/print-expr expr))

(defn rep [str]
  (PRINT (EVAL (READ str) env)))

;; TODO Add support for history
(defn readline [str]
  (do (print str) (flush)
      (read-line)))

(defn -main [& args]
  (loop []
    (let [line (readline "user> ")]
      (when line
        (when-not (re-seq #"^\s*$|^\s*;.*$" line)
          (doseq [item (util/try-and rep "Error while parsing expression:\n" line)]
            (println item)))
        (recur)))))
