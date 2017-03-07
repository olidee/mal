(ns oli-clj.reader
  (:require [clojure.string :as str]))

(def regexp #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:\\.|[^\\\"])*\"|;.*|[^\s\[\]{}('\"`,;)]*)")

(defn tokenizer [str]
  (into [] (comp (map #(second %))
                 (filter #(not (empty? %))))
        (re-seq regexp str)))

(defn read-form
  ([tokens]
    (read-form [] tokens))
  ([ast [t & ts]]
    (case t
      ;; Bug here. If unmatched paren exists in string, (read-form ts) will exit with the nil t case,
      ;; and only return ast. So when we destructure into [new-ast new-ts] it is actually destructuring on just the values in ast.
      "(" (let [[new-ast new-ts] (read-form ts)]
            (recur (conj ast new-ast) new-ts))
      ")" [ast ts]
      nil ast
      (recur (conj ast (str t)) ts))))

(defn read-expr [str]
  (read-form (tokenizer str)))


;; "(+ 1 2 3)"

;; (read-form ["(" "+" "1" "2" ")"])
;; (read-form [] ["(" "+" "1" "2" ")"])


;;   (read-form ["+" "1" "2" ")"])
;;   (read-form [] ["+" "1" "2" ")"])
;;     (read-form ["+"] ["1" "2" ")"])
;;       (read-form ["+" "1"] ["2" ")"])
;;         (read-form ["+" "1" "2"] [")"])
;;           (read-form ["+" "1" "2"] nil)
;;             ["+" "1" "2"]

;; [( + 1 2 3 )] -> [] [( + 1 2 3 )] :18
;; [+ 1 2 3 )] -> [] [+ 1 2 3 )] :23
;;   [+] [1 2 3 )] :26
;;   [+ 1] [2 3 )] :26
;;   [+ 1 2] [3 )] :26
;;   [+ 1 2 3] [)] :26
;;   [+ 1 2 3] [] :21
;;   return [+ 1 2 3]
;; [[+ 1 2 3]] nil :24
;; [[+ 1 2 3]]

;; "(+ 1 (+ 2 3))"
;; [( + 1 ( + 2 3 ) )] -> [] [( + 1 ( + 2 3 ) )] :18
;; [+ 1 ( + 2 3 ) )] -> [] [+ 1 ( + 2 3 ) )] :23
;;   [+] [1 ( + 2 3 ) )] :26
;;   [+ 1] [( + 2 3 ) )] :26
;;   [+ 2 3 ) )] -> [] [+ 2 3 ) )] :23
;;     [+] [2 3 ) )] :26
;;     [+ 2] [3 ) )] :26
;;     [+ 2 3] [) )] :26
;;     [+ 2 3] [)]   :25
;;     [+ 2 3] nil   :21
;;     [+ 2 3] return
;;   [+ 1 [+ 2 3]] nil :24
;;   [+ 1 [+ 2 3]] return
;; [+ 1 [+ 2 3]] return
