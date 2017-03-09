(ns mal.step0-repl)

(defn READ [str]
  str)

(defn EVAL [ast env]
  ast)

(defn PRINT [expr]
  expr)

(defn rep [str]
  (PRINT (EVAL (READ str) "")))

(defn readline [str]
  (do (print str) (flush)
      (read-line)))

(defn -main [& args]
  (loop []
    (let [line (readline "user> ")]
      (when line
        (when-not (re-seq #"^\s*$|^\s*;.*$" line)
          (println (rep line)))
        (recur)))))
