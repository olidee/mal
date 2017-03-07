(ns oli-clj.step1-read-print
  (:require [oli-clj.reader :as reader]
            [oli-clj.printer :as printer]))

(defn READ [str]
  (reader/read-expr str))

(defn EVAL [ast env]
  ast)

(defn PRINT [expr]
  (printer/print-expr expr))

(defn rep [str]
  (PRINT (EVAL (READ str) {})))

(defn readline [str]
  (do (print str) (flush)
      (read-line)))

(defn -main [& args]
  (loop []
    (let [line (readline "user> ")]
      (when line
        (when-not (re-seq #"^\s*$|^\s*;.*$" line)
          (doseq [item (rep line)]
            (println item)))
        (recur)))))
