;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.filter-test
  (:require [clj-helper.filter :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])))

(deftest contains-ci-test
  (testing "contains-ci? case-insensitive match"
    (is (true? (sut/contains-ci? "Hello World" "world")))
    (is (true? (sut/contains-ci? "Deutschland" "tschl")))
    (is (false? (sut/contains-ci? "Hello" "xyz")))
    (is (nil? (sut/contains-ci? nil "test")))
    (is (nil? (sut/contains-ci? "test" nil))))
  (testing "backward-compatibility searchfilter alias"
    (is (true? (sut/searchfilter "Hello World" "world")))))

(deftest any-key-contains-test
  (testing "any-key-contains? across specified keys"
    (let [data {:name "Bruno" :country "Germany" :role "Developer"}]
      (is (true? (sut/any-key-contains? data "bruno" [:name :country])))
      (is (true? (sut/any-key-contains? data "germany" [:name :country])))
      (is (nil? (sut/any-key-contains? data "developer" [:name :country])))))
  (testing "backward-compatibility keyfilter alias"
    (let [data {:name "Bruno" :country "Germany"}]
      (is (true? (sut/keyfilter data "bruno" [:name]))))))

(deftest multi-filter-test
  (testing "multi-filter? exact attribute matches"
    (let [data {:id 1 :name "Bruno" :active true :dept "IT"}]
      (is (true? (sut/multi-filter? data {:dept "IT" :active true})))
      (is (false? (sut/multi-filter? data {:dept "Sales"})))
      (is (true? (sut/multi-filter? data {:dept "*"})))
      (is (true? (sut/multi-filter? data {:dept :any})))
      (is (true? (sut/multi-filter? data {:all "bruno"})))
      (is (false? (sut/multi-filter? data {:blacklist #{1}})))
      (is (true? (sut/multi-filter? data {:blacklist #{2 3}})))))
  (testing "backward-compatibility multifilter alias"
    (let [data {:id 1 :dept "IT"}]
      (is (true? (sut/multifilter data {:dept "IT"}))))))

(deftest filter-by-attrs-test
  (testing "filter-by-attrs over collection"
    (let [coll [{:id 1 :role "admin" :status "active"}
                {:id 2 :role "user" :status "active"}
                {:id 3 :role "user" :status "inactive"}]]
      (is (= [{:id 2 :role "user" :status "active"}]
             (sut/filter-by-attrs coll {:role "user" :status "active"})))
      (is (= [{:id 2 :role "user" :status "active"}
              {:id 3 :role "user" :status "inactive"}]
             (sut/filter-by-attrs coll {:role "user" :status "*"})))
      (is (= [{:id 1 :role "admin" :status "active"}
              {:id 3 :role "user" :status "inactive"}]
             (sut/filter-by-attrs coll {:blacklist #{2}}))))))
