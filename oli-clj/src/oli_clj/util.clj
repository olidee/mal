(ns oli-clj.util)

(defn try-and [f msg & args]
  (try
    (apply f args)
    (catch Exception e (println msg (.getMessage e)))))

(defn parse [str]
  (try
    (Integer/parseInt str)
    (catch NumberFormatException e
      (try
        (Float/parseFloat str)
        (catch NumberFormatException e
          str)))))
