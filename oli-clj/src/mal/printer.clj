(ns mal.printer)

(defn format-expr [expr]
  (cond
    (vector? expr) (str "(" (clojure.string/join " " (map #(format-expr %) expr)) ")")
    :else expr))

(defn print-expr [exprs]
  (map #(format-expr %) exprs))
