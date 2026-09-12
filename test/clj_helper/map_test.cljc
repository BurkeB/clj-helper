;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.map-test
  (:require [clj-helper.map :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [clojure.string :as str]))

(deftest update-values-test
  (testing "update-values modifies all map values with ufn"
    (is (= (sut/update-values {:a 1 :b 2 :c 3} inc)
           {:a 2 :b 3 :c 4}))
    (is (= (sut/update-values {:name "bruno"} str/upper-case)
           {:name "BRUNO"}))))

(deftest key-equals-test
  (testing "key-equals? and keyqual with non-sequence keys"
    (let [data {:id 5 :a "hallo" :b :test}]
      (is (true? (sut/key-equals? data :id 5)))
      (is (true? (sut/keyqual data :id 5)))
      (is (true? (sut/key-equals? data :a "hallo")))
      (is (true? (sut/key-equals? data :b :test)))
      (is (false? (sut/key-equals? data :id 99)))))
  (testing "key-equals? with keys sequence (nested path)"
    (let [data {:nesting1 {:id 5 :a "hallo" :b :test}
                :nesting2 {:nesting3 {:id 6
                                       :a "hallo2"
                                       :b :test2}}}]
      (is (true? (sut/key-equals? data [:nesting1 :id] 5)))
      (is (true? (sut/key-equals? data [:nesting1 :a] "hallo")))
      (is (true? (sut/key-equals? data [:nesting1 :b] :test)))

      (is (true? (sut/key-equals? data [:nesting2 :nesting3 :id] 6)))
      (is (true? (sut/key-equals? data [:nesting2 :nesting3 :a] "hallo2")))
      (is (true? (sut/key-equals? data [:nesting2 :nesting3 :b] :test2))))))
