(ns oli-clj.printer)

(defn format-expr [expr]
  (cond
    (vector? expr) (str "(" (clojure.string/join " " (map #(format-expr %) expr)) ")")
    (string? expr) (str expr)))

(defn print-expr [exprs]
  (map #(format-expr %) exprs))
