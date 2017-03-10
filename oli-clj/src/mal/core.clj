(ns mal.core
  (require [mal.printer :as printer]))

(def ns {"+"      (fn [x y] (+ x y))
         "-"      (fn [x y] (- x y))
         "*"      (fn [x y] (* x y))
         "/"      (fn [x y] (/ x y))
         "prn"    (fn [str] (printer/print-expr str))
         "list"   (fn [& args] (vec args))
         "list?"  (fn [val] (vector? val))
         "empty?" empty?
         "count"  count
         "="      =
         "<"      <
         "<="     <=
         ">"      >
         ">="     >=})
