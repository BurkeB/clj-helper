;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.compression-test
  (:require [clj-helper.compression :as sut]
            [clj-helper.string :refer [get-random-code]]
            [clojure.test :refer :all]))

(deftest compression-test
  (testing "String->xz and xz->string roundtrip with test-string \"abc\""
    (let [test-string "abc"]
      (is (=
           (sut/xz->string (sut/string->xz test-string))
           test-string))))
  (testing "String->xz->base64 and base64->xz->string roundtrip with test-string \"abc\""
    (let [test-string "abc"]
      (is (=
           (sut/base64->xz->string (sut/string->xz->base64 test-string))
           "abc"))))
  (testing "compress->base64 and base64->decompress alias roundtrip"
    (let [test-string "payload 12345"]
      (is (= (sut/base64->decompress (sut/compress->base64 test-string))
             test-string))))
  (let [random-string (get-random-code 300)]
    (testing (str "String->xz and xz->string roundtrip with random string: " random-string)
      (is (= (-> random-string
                 sut/string->xz
                 sut/xz->string)
             (-> random-string
                 sut/compress->base64
                 sut/base64->decompress)
             random-string)))))
