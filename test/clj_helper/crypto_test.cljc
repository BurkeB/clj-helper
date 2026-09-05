;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.crypto-test
  (:require [clj-helper.crypto :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])))

(deftest crypto-hashing-test
  (testing "string->md5 generates standard hex digest"
    (is (= (sut/string->md5 "test")
           "098f6bcd4621d373cade4e832627b4f6"))
    (is (= (sut/string->md5 "clj-helper")
           "3c662a4139e8d8d386cea6a9b95e63cb")))

  (testing "string->sha1 generates standard hex digest"
    (is (= (sut/string->sha1 "test")
           "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"))
    (is (= (sut/string->sha1 "clj-helper")
           "c51f0b836ae733dea4da7bb442a6ef9c4c72eab0")))

  (testing "string->sha256 generates standard hex digest"
    (is (= (sut/string->sha256 "test")
           "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"))
    (is (= (sut/string->sha256 "clj-helper")
           "57825ba461693e147cc5a78c4b642baa6cbf3c4ebe0507873d16e35b9e7c63e0"))))
