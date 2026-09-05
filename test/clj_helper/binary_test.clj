;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.binary-test
  (:require [clojure.test :refer :all]
            [clj-helper.binary :refer :all]))

(deftest base64-encoding-test
  (testing "String->ByteArray with test-string \"abc\""
    (let [test-string "abc"]
      (is (=
           (vec (string->byte-array test-string))
           '(97 98 99)))))
  (testing "ByteArray->String with test-string \"abc\""
    (let [test-string "abc"]
      (is (=
           (byte-array->string (byte-array (doall (map byte '(97 98 99)))))
           "abc"
           ))))
  (testing "Encode Hallo to SGFsbG8="
    (let [test-string "Hallo"
          test-result "SGFsbG8="]
      (is (=
           (byte-array->base64 (string->byte-array test-string))
           test-result))))
  (testing "Directfunction Encode Hallo to SGFsbG8="
    (let [test-string "Hallo"
          test-result "SGFsbG8="]
      (is (=
           (string->base64 test-string)
           test-result))))
  (testing "Decode VGVzdA== to Test"
    (let [test-string "Test"
          test-base64 "VGVzdA=="]
      (is (=
           (byte-array->string (base64->byte-array test-base64))
           test-string))))
  (testing "Directfunction Decode VGVzdA== to Test"
    (let [test-string "Test"
          test-base64 "VGVzdA=="]
      (is (=
           (base64->string test-base64)
           test-string))))
  (let [random-string "fsfdfdggfdfdg"]
    (testing (str "Base64 Encode/Decode Roundtrip with random string: " random-string)
      (= (-> random-string
             string->byte-array
             byte-array->base64
             base64->byte-array
             byte-array->string)
         random-string))))

