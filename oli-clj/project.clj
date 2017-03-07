(defproject oli-clj "0.0.1-SNAPSHOT"
  :description "Make-A-Lisp"

  :dependencies [[org.clojure/clojure "1.8.0-RC4"]
                 [org.clojure/tools.reader "0.8.3"]
                 [net.n01se/clojure-jna "1.0.0"]]

  ;; To run a step with correct readline behavior:
  ;;   lein trampoline with-profile stepX run
  ;; To generate a executable uberjar (in target/) for a step:
  ;;   lein with-profile stepX repl
  :profiles {:step0 {:main oli-clj.step0-repl
                     :uberjar-name "step0_repl.jar"
                     :aot [oli-clj.step0-repl]}
             :step1 {:main oli-clj.step1-read-print
                     :uberjar-name "step1_read_print.jar"
                     :aot [oli-clj.step1-read-print]}
             :step2 {:main oli-clj.step2-eval
                     :uberjar-name "step2_eval.jar"
                     :aot [oli-clj.step2-eval]}
             :step3 {:main oli-clj.step3-env
                     :uberjar-name "step3_env.jar"
                     :aot [oli-clj.step3-env]}
             :step4 {:main oli-clj.step4-if-fn-do
                     :uberjar-name "step4_if_fn_do.jar"
                     :aot [oli-clj.step4-if-fn-do]}
             :step5 {:main oli-clj.step5-tco
                     :uberjar-name "step5_tco.jar"
                     :aot [oli-clj.step5-tco]}
             :step6 {:main oli-clj.step6-file
                     :uberjar-name "step6_file.jar"
                     :aot [oli-clj.step6-file]}
             :step7 {:main oli-clj.step7-quote
                     :uberjar-name "step7_quote.jar"
                     :aot [oli-clj.step7-quote]}
             :step8 {:main oli-clj.step8-macros
                     :uberjar-name "step8_macros.jar"
                     :aot [oli-clj.step8-macros]}
             :step9 {:main oli-clj.step9-try
                     :uberjar-name "step9_try.jar"
                     :aot [oli-clj.step9-try]}
             :stepA {:main oli-clj.stepA-mal
                     :uberjar-name "stepA_mal.jar"
                     :aot [oli-clj.stepA-mal]}})

