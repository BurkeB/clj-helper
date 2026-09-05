;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.edn-test
  (:require [clj-helper.edn :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])))

(deftest edn-roundtrip-test
  (testing "serialize and deserialize edn"
    (let [data {:name "Alice" :age 30 :tags [:clj :cljs] :scores [1 2 3]}
          serialized (sut/serialize-edn data)
          deserialized (sut/deserialize-edn serialized)]
      (is (string? serialized))
      (is (= data deserialized))))

  (testing "deserialize edn string directly"
    (is (= (sut/deserialize-edn "{:a 1 :b [2 3]}")
           {:a 1 :b [2 3]}))))
