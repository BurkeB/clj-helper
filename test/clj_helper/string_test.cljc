;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.string-test
  (:require [clj-helper.string :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])))

(deftest str-equality-test
  (testing "str= and eq? equivalence"
    (is (true? (sut/str= 123 "123")))
    (is (true? (sut/eq? 123 "123")))
    (is (true? (sut/eq? :foo ":foo")))
    (is (false? (sut/eq? "a" "b")))))

(deftest set->str->set
  (testing "numbers"
    (let [set #{234 534 523 41 3 123 5 35 6456 76 767 563 2}]
      (is (= set
             (-> set
                 sut/set->str
                 sut/str->set)))))
  (testing "string"
    (let [set (into #{} (clojure.string/split "dfkjsdkfjs df sdf wcr wer vfcv xa sd qwe zuk ghh mghm gbnb cvvd" #" "))]
      (is (= set
             (-> set
                 sut/set->str
                 sut/str->set)))))
  (testing "mixed"
    (let [set #{2334234 :f :g :h 42 "h" "f" 34 2 13 "ddssd" "klk" "ioi" "üüü" "wwewe" "xxx" :a :c 5 6 7 76}]
      (is (= set
             (-> set
                 sut/set->str
                 sut/str->set))))))

(deftest shorten-test
  (testing "shorten strings"
    (is (= (sut/shorten "Hello World" 5) "Hello"))
    (is (= (sut/shorten "Hi" 5) "Hi"))
    (is (nil? (sut/shorten nil 5)))))

(deftest safe-name-test
  (testing "safe-name conversion"
    (is (= (sut/safe-name :foo) "foo"))
    (is (= (sut/safe-name "bar") "bar"))
    (is (= (sut/safe-name 123) ""))
    (is (= (sut/safe-name nil) ""))))

(deftest random-code-test
  (testing "get-random-code and rand-code generate expected length"
    (let [code (sut/get-random-code 16)]
      (is (= (count code) 16))
      (is (string? code)))
    (let [code (sut/rand-code 8)]
      (is (= (count code) 8))
      (is (string? code))))
  (testing "get-secure-random-code generates expected length"
    (let [code (sut/get-secure-random-code 20)]
      (is (= (count code) 20))
      (is (string? code)))))

(deftest unique-id-test
  (testing "unique-id format and length"
    (let [id1 (sut/unique-id)
          id2 (sut/unique-id "usr")]
      (is (string? id1))
      (is (<= (count id1) 32))
      (is (string? id2))
      (is (<= (count id2) 32))
      (is (not= id1 id2))))
  (testing "backward-compatible get-unique-id"
    (let [id (sut/get-unique-id "USR")]
      (is (string? id))
      (is (<= (count id) 32)))))

(deftest parse-int-test
  (testing "parse-int conversions"
    (is (= (sut/parse-int "123") 123))
    (is (= (sut/parse-int "-42") -42))
    (is (nil? (sut/parse-int "invalid")))
    (is (nil? (sut/parse-int 123)))
    (is (nil? (sut/parse-int nil)))))

(deftest quote-text-test
  (testing "quote-text format"
    (is (= (sut/quote-text "abc") "»abc«"))))

(deftest parameterstring-test
  (testing "parameterstring formatting with legacy kwargs"
    (is (= (sut/parameterstring {:a "1" :b "2"})
           "a=\"1\", b=\"2\"")))
  (testing "param-string formatting with concise kwargs"
    (is (= (sut/param-string {:k "val"} :assign ":" :quote "'")
           "k:'val'"))))
