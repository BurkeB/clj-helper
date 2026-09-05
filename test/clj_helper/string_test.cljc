;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.string-test
  (:require [clj-helper.string :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer :all :include-macros true])))


(deftest set->str->set
  (testing "numbers"
    (let [set #{234  534 523 41 3 123 5 35 6456  76 767 563 2}]
      (is (= set
             (-> set
                 sut/set->str
                 sut/str->set)))))
  (testing "string"
    (let [set  (into #{} (clojure.string/split "dfkjsdkfjs df sdf wcr wer vfcv xa sd qwe zuk ghh mghm gbnb cvvd" #" "))]
      (is (= set
             (-> set
                 sut/set->str
                 sut/str->set)))))
  (testing "mixed"
    (let [set  #{2334234 :f :g :h 42 "h" "f" 34 2 13 "ddssd" "klk" "ioi" "üüü" "wwewe" "xxx" :a :c 5 6 7 76}]
      (is (= set
             (-> set
                 sut/set->str
                 sut/str->set))))))



;;;TODO Add tests for parameterstring
