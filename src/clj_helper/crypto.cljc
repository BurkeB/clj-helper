;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.crypto
  #?(:cljs (:require [goog.crypt]
                     [goog.crypt.Md5])
     :clj (:require [clj-commons.digest :as digest])))

#?(:cljs
   (defn string->md5 [str]
     (goog.crypt/byteArrayToHex
      (let [md5 (goog.crypt.Md5.)]
        (.update md5 (goog.crypt/stringToUtf8ByteArray str))
        (.digest md5)))))

#?(:clj
   (defn string->sha1 [str]
     (digest/sha1 str)))
