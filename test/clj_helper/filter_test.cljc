;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
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

(deftest searchfilter-test
  (testing "searchfilter case-insensitive match"
    (is (true? (sut/searchfilter "Hello World" "world")))
    (is (true? (sut/searchfilter "Deutschland" "tschl")))
    (is (false? (sut/searchfilter "Hello" "xyz")))
    (is (nil? (sut/searchfilter nil "test")))
    (is (nil? (sut/searchfilter "test" nil)))))

(deftest keyfilter-test
  (testing "keyfilter across specified keys"
    (let [data {:name "Bruno" :country "Germany" :role "Developer"}]
      (is (true? (sut/keyfilter data "bruno" [:name :country])))
      (is (true? (sut/keyfilter data "germany" [:name :country])))
      (is (nil? (sut/keyfilter data "developer" [:name :country]))))))

(deftest multifilter-test
  (testing "multifilter exact attribute matches"
    (let [data {:id 1 :name "Bruno" :active true :dept "IT"}]
      (is (true? (sut/multifilter data {:dept "IT" :active true})))
      (is (false? (sut/multifilter data {:dept "Sales"})))
      (is (true? (sut/multifilter data {:dept "*"})))
      (is (true? (sut/multifilter data {:dept :any}))))))
