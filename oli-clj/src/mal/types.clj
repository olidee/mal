(ns mal.types)

(defmulti parse
  (fn [val]
    (cond
      (re-matches #"\d+(\.\d+)?" val) :number
      (re-matches #"\"[^\"]\"" val) :string
      :symbol)))

(defmethod parse :string [v] v)
(defmethod parse :symbol [v]
  (str "#'" v))
(defmethod parse :number [v]
  (try
    (Integer/parseInt v)
    (catch NumberFormatException e
      (Float/parseFloat v))))
