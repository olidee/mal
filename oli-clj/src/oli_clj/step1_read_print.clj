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

(defn try-rep [str]
  (try
    (rep str)
    (catch Exception e (println "Parse error: " (.getMessage e)))))

(defn -main [& args]
  (loop []
    (let [line (readline "user> ")]
      (when line
        (when-not (re-seq #"^\s*$|^\s*;.*$" line)
          (doseq [item (try-rep line)]
            (println item)))
        (recur)))))
