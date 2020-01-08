(ns gorilla-plot.vega-test
  (:require [clojure.test :refer [is deftest]]))

(defn my-test-fixture [f]
  (f))

;; (use-fixtures :once my-test-fixture)

(defn add [x y] (+ x y))

(deftest add-x-to-y-a-few-times
  (is (= 5 (add 2 3)))
  (is (= 5 (add 1 4)))
  (is (= 5 (add 3 2))))

#_(run-all-tests)

#_(.addShutdownHook
   (Runtime/getRuntime)
   (proxy [Thread] []
     (run []
       (run-all-tests))))
