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



(deftest remove-nth-test
  (testing "vector remove-nth index out of bound"
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] -1) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector remove-nth"
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 0) [1 2 3 4 5 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 1) [0 2 3 4 5 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 2) [0 1 3 4 5 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 3) [0 1 2 4 5 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 4) [0 1 2 3 5 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 5) [0 1 2 3 4 6 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 6) [0 1 2 3 4 5 7 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 7) [0 1 2 3 4 5 6 8 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 8) [0 1 2 3 4 5 6 7 9]))
    (is (= (remove-nth [0 1 2 3 4 5 6 7 8 9] 9) [0 1 2 3 4 5 6 7 8]))))


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


(deftest move-test
  (testing "vector move index out of bound"
    (is (= (move [0 1 2 3 4 5 6 7 8 9] -1 1) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 1 10) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector move"
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 0 1) [1 0 2 3 4 5 6 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 9 0) [9 0 1 2 3 4 5 6 7 8]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 4 0) [4 0 1 2 3 5 6 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 4 9) [0 1 2 3 5 6 7 8 9 4]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 4 6) [0 1 2 3 5 6 4 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 6 4) [0 1 2 3 6 4 5 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 8 9) [0 1 2 3 4 5 6 7 9 8]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 3 1) [0 3 1 2 4 5 6 7 8 9]))
    ))


(deftest mapvec-to-map-test
  (is (= (mapvec-to-map [{:id 2 :cc 44}{:id 1 :cc 22}])
         {2 {:id 2 :cc 44}
          1 {:id 1 :cc 22}})))

(deftest get-by-test
  (testing "get with keyword key"
    (let [data [{:id 5 :a 1}
                {:id 6 :a 2}
                {:id 1 :a 3}
                {:id 7 :a 4}]]
      (is (= (get-by data :id 1) {:id 1 :a 3}))
      (is (= (get-by data :id 6) {:id 6 :a 2}))))
  (testing "get with string key"
    (let [data [{:id "5" :a 1}
                {:id "6" :a 2}
                {:id "1" :a 3}
                {:id "7" :a 4}]]
      (is (= (get-by data :id "1") {:id "1" :a 3}))
      (is (= (get-by data :id "6") {:id "6" :a 2}))
      (is (not= (get-by data :id 6) {:id "6" :a 2}))
      (is (= (get-by data :id 6) nil))))
  (testing "get with vector key"
    (let [data [{:data {:id "5" :a 1}}
                {:data {:id "6" :a 2}}
                {:data {:id "1" :a 3}}
                {:data {:id "7" :a 4}}]]
      (is (= (get-by data [:data :id] "1") {:data {:id "1" :a 3}}))
      (is (= (get-by data [:data :id] "6") {:data {:id "6" :a 2}}))
      (is (not= (get-by data [:data :id] 6) {:data {:id "6" :a 2}})))))
