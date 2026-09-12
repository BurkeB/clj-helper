;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.vector-test
  (:require [clojure.test :refer :all]
            [clj-helper.vector :refer :all]
            [clojure.string :refer [upper-case lower-case]]))

(deftest move-left-right-test
  (testing "can-move-left? test"
    (is (can-move-left? [0 1 2 3 4 5 6 7 8 9] 1))
    (is (can-move-left? [0 1 2 3 4 5 6 7 8 9] 9))
    (is (not (can-move-left? [0 1 2 3 4 5 6 7 8 9] 0)))
    (is (not (can-move-left? [0 1 2 3 4 5 6 7 8 9] 10)))
    (is (not (can-move-left? [0 1 2 3 4 5 6 7 8 9] -1)))
    (is (not (can-move-left? [0 1 2 3 4 5 6 7 8 9] 10))))
  (testing "can-move-right? test"
    (is (can-move-right? [0 1 2 3 4 5 6 7 8 9] 0))
    (is (can-move-right? [0 1 2 3 4 5 6 7 8 9] 8))
    (is (not (can-move-right? [0 1 2 3 4 5 6 7 8 9] 9)))
    (is (not (can-move-right? [0 1 2 3 4 5 6 7 8 9] 10)))
    (is (not (can-move-right? [0 1 2 3 4 5 6 7 8 9] -1)))
    (is (not (can-move-right? [0 1 2 3 4 5 6 7 8 9] 10))))
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

(deftest move-cycled-test
  (testing "regular moving left test"
    (is (= (move-left-cycled [0 1 2 3 4 5 6 7 8 9] 9) [0 1 2 3 4 5 6 7 9 8]))
    (is (= (move-left-cycled [0 1 2 3 4 5 6 7 8 9] 1) [1 0 2 3 4 5 6 7 8 9])))
  (testing "regular moving right test"
    (is (= (move-right-cycled [0 1 2 3 4 5 6 7 8 9] 0) [1 0 2 3 4 5 6 7 8 9]))
    (is (= (move-right-cycled [0 1 2 3 4 5 6 7 8 9] 8) [0 1 2 3 4 5 6 7 9 8])))
  (testing "index out of bound move-left-cycled test"
    (is (= (move-left-cycled [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (move-left-cycled [0 1 2 3 4 5 6 7 8 9] -1) [0 1 2 3 4 5 6 7 8 9])))
  (testing "index out of bound move-right-cycled test"
    (is (= (move-right-cycled [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (move-right-cycled [0 1 2 3 4 5 6 7 8 9] -1) [0 1 2 3 4 5 6 7 8 9])))
  (testing "cycling moving left test"
    (is (= (move-left-cycled [0 1 2 3 4 5 6 7 8 9] 0) [1 2 3 4 5 6 7 8 9 0])))
  (testing "cycling moving right test"
    (is (= (move-right-cycled [0 1 2 3 4 5 6 7 8 9] 9) [9 0 1 2 3 4 5 6 7 8]))))

(deftest move-test
  (testing "vector move index out of bound"
    (is (= (move [0 1 2 3 4 5 6 7 8 9] -1 1) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 1 10 :append-ok? true) [0 2 3 4 5 6 7 8 9 1]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 1 10) [0 1 2 3 4 5 6 7 8 9])))
  (testing "vector move"
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 0 1) [1 0 2 3 4 5 6 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 9 0) [9 0 1 2 3 4 5 6 7 8]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 4 0) [4 0 1 2 3 5 6 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 4 9) [0 1 2 3 5 6 7 8 9 4]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 4 6) [0 1 2 3 5 6 4 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 6 4) [0 1 2 3 6 4 5 7 8 9]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 8 9) [0 1 2 3 4 5 6 7 9 8]))
    (is (= (move [0 1 2 3 4 5 6 7 8 9] 3 1) [0 3 1 2 4 5 6 7 8 9]))))

(deftest swap-at-test
  (testing "swap-at elements"
    (is (= (swap-at [:a :b :c :d] 0 3) [:d :b :c :a]))
    (is (= (swap-at [:a :b :c :d] 1 2) [:a :c :b :d]))
    (is (= (swap-at [:a :b :c :d] 1 1) [:a :b :c :d]))
    (is (= (swap-at [:a :b :c :d] -1 2) [:a :b :c :d]))
    (is (= (swap-at [:a :b :c :d] 0 10) [:a :b :c :d]))))

(deftest remove-at-test
  (testing "vector remove-at"
    (is (= (remove-at [0 1 2 3 4 5 6 7 8 9] 10) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (remove-at [0 1 2 3 4 5 6 7 8 9] -1) [0 1 2 3 4 5 6 7 8 9]))
    (is (= (remove-at [0 1 2 3 4 5 6 7 8 9] 0) [1 2 3 4 5 6 7 8 9]))
    (is (= (remove-at [0 1 2 3 4 5 6 7 8 9] 4) [0 1 2 3 5 6 7 8 9]))))

(deftest insert-at-test
  (testing "vector insert-at"
    (is (= (insert-at [0 1 2 3] 0 "x") ["x" 0 1 2 3]))
    (is (= (insert-at [0 1 2 3] 2 "x") [0 1 "x" 2 3]))
    (is (= (insert-at [0 1 2 3] 4 "x") [0 1 2 3 "x"]))
    (is (thrown? AssertionError (insert-at [0 1 2 3] -1 "x")))
    (is (thrown? AssertionError (insert-at [0 1 2 3] 5 "x"))))
  (testing "vector insert-at-safe"
    (is (= (insert-at-safe [0 1 2 3] 1 "x") [0 "x" 1 2 3]))
    (is (= (insert-at-safe [0 1 2 3] -1 "x") [0 1 2 3]))
    (is (= (insert-at-safe [0 1 2 3] 10 "x") [0 1 2 3]))))

(deftest vconj-test
  (testing "vconj returns vector"
    (is (= (vconj [1 2] 3) [1 2 3]))
    (is (= (vconj '(1 2) 3) [1 2 3]))
    (is (vector? (vconj '(1 2) 3)))))

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


(deftest get-next-prev-index-cycled
  (testing "get-next-index-cycled"
    (is (= (get-next-index-cycled [0 1 2 3 4 5 6 7 8 9] 0) 1))
    (is (= (get-next-index-cycled [0 1 2 3 4 5 6 7 8 9] 9) 0))
    (is (= (get-next-index-cycled [0 1 2 3 4 5 6 7 8 9] 10) 0))
    (is (= (get-next-index-cycled [0 1 2 3 4 5 6 7 8 9] -1) 0)))
  (testing "get-prev-index-cycled"
    (is (= (get-prev-index-cycled [0 1 2 3 4 5 6 7 8 9] 0) 9))
    (is (= (get-prev-index-cycled [0 1 2 3 4 5 6 7 8 9] 9) 8))
    (is (= (get-prev-index-cycled [0 1 2 3 4 5 6 7 8 9] 10) 9))
    (is (= (get-prev-index-cycled [0 1 2 3 4 5 6 7 8 9] -1) 9))))

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
  (testing "get with fn key"
    (let [data [{:id "str1" :a 1}
                {:id "str2" :a 2}
                {:id "str3" :a 3}
                {:id "str4" :a 4}]]
      (is (= (get-by data (comp upper-case :id) "STR2") {:id "str2" :a 2})))
    (let [data [{:id :alpha :a 1}
                {:id :beta :a 2}
                {:id :gamma :a 3}
                {:id :delta :a 4}]]
      (is (= (get-by data (comp name :id) "gamma") {:id :gamma :a 3}))))
  (testing "get with vector key"
    (let [data [{:data {:id "5" :a 1}}
                {:data {:id "6" :a 2}}
                {:data {:id "1" :a 3}}
                {:data {:id "7" :a 4}}]]
      (is (= (get-by data [:data :id] "1") {:data {:id "1" :a 3}}))
      (is (= (get-by data [:data :id] "6") {:data {:id "6" :a 2}}))
      (is (not= (get-by data [:data :id] 6) {:data {:id "6" :a 2}}))))
  (testing "get with string map-key (e.g. JSON map)"
    (let [data [{"id" 1 :name "A"} {"id" 2 :name "B"}]]
      (is (= (get-by data "id" 2) {"id" 2 :name "B"}))
      (is (nil? (get-by data "id" 99)))))
  (testing "get-by false match prevention"
    (let [data [{:foo 1} {:bar 2}]]
      (is (nil? (get-by data :missing :missing))))))

(deftest remove-by-test
  (testing "remove-by existing element"
    (let [data [{:id 1 :name "A"} {:id 2 :name "B"} {:id 3 :name "C"}]]
      (is (= (remove-by data :id 2)
             [{:id 1 :name "A"} {:id 3 :name "C"}]))))
  (testing "remove-by non-existing element does not throw NPE"
    (let [data [{:id 1 :name "A"} {:id 2 :name "B"}]]
      (is (= (remove-by data :id 999)
             data))
      (is (= (remove-by [1 2 3] :missing 42)
             [1 2 3])))))

(deftest get-index-by-test
  (testing "get-index-by keyword"
    (let [data [{:id 10} {:id 20} {:id 30}]]
      (is (= (get-index-by data :id 20) 1))
      (is (nil? (get-index-by data :id 99)))))
  (testing "get-index-by vector path"
    (let [data [{:user {:id "u1"}} {:user {:id "u2"}}]]
      (is (= (get-index-by data [:user :id] "u2") 1))
      (is (nil? (get-index-by data [:user :id] "u99")))))
  (testing "get-index-by string key"
    (let [data [{"code" "A"} {"code" "B"}]]
      (is (= (get-index-by data "code" "B") 1)))))

