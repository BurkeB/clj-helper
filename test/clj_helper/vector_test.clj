(ns clj-helper.vector-test
  (:require [clojure.test :refer :all]
            [clj-helper.vector :refer :all]))

(deftest move-test
  (testing "vector move left index out of bound"
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 0) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector move left"
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 1) [1 0 2 3 4 5 6 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 2) [0 2 1 3 4 5 6 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 3) [0 1 3 2 4 5 6 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 4) [0 1 2 4 3 5 6 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 5) [0 1 2 3 5 4 6 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 6) [0 1 2 3 4 6 5 7 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 7) [0 1 2 3 4 5 7 6 8 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 8) [0 1 2 3 4 5 6 8 7 9]))
    (is (= (move-left [0 1 2 3 4 5 6 7 8 9] 9) [0 1 2 3 4 5 6 7 9 8])))
  (testing "vector move right index out of bound"
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 9) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector move right"
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 0) [1 0 2 3 4 5 6 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 1) [0 2 1 3 4 5 6 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 2) [0 1 3 2 4 5 6 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 3) [0 1 2 4 3 5 6 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 4) [0 1 2 3 5 4 6 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 5) [0 1 2 3 4 6 5 7 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 6) [0 1 2 3 4 5 7 6 8 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 7) [0 1 2 3 4 5 6 8 7 9]))
    (is (= (move-right [0 1 2 3 4 5 6 7 8 9] 8) [0 1 2 3 4 5 6 7 9 8]))))



(deftest remove-test
  (testing "vector remove index out of bound"
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] -1) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector remove"
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 0) [1 2 3 4 5 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 1) [0 2 3 4 5 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 2) [0 1 3 4 5 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 3) [0 1 2 4 5 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 4) [0 1 2 3 5 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 5) [0 1 2 3 4 6 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 6) [0 1 2 3 4 5 7 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 7) [0 1 2 3 4 5 6 8 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 8) [0 1 2 3 4 5 6 7 9]))
    (is (= (remove [0 1 2 3 4 5 6 7 8 9] 9) [0 1 2 3 4 5 6 7 8]))))


(deftest insert-test
  (testing "vector insert index out of bound"
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" -1) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector insert"
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 0) ["x" 0 1 2 3 4 5 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 1) [0 "x" 1 2 3 4 5 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 2) [0 1 "x" 2 3 4 5 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 3) [0 1 2 "x" 3 4 5 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 4) [0 1 2 3 "x" 4 5 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 5) [0 1 2 3 4 "x" 5 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 6) [0 1 2 3 4 5 "x" 6 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 7) [0 1 2 3 4 5 6 "x" 7 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 8) [0 1 2 3 4 5 6 7 "x" 8 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 9) [0 1 2 3 4 5 6 7 8 "x" 9]))
    (is (= (insert [0 1 2 3 4 5 6 7 8 9] "x" 10) [0 1 2 3 4 5 6 7 8 9 "x"])) ;;; insert allows appending!
    ))
