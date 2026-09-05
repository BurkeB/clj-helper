;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.set-test
  (:require [clj-helper.set :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])))

(deftest toggle-test
  (testing "toggle adds element if absent"
    (is (= (sut/toggle #{:a :b} :c) #{:a :b :c}))
    (is (= (sut/toggle #{} :a) #{:a}))
    (is (= (sut/toggle nil :a) #{:a})))

  (testing "toggle removes element if present"
    (is (= (sut/toggle #{:a :b :c} :b) #{:a :c}))
    (is (= (sut/toggle #{:a} :a) #{}))))
