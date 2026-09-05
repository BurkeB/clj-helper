;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.crypto
  #?(:cljs (:require [goog.crypt :as crypt]
                     [goog.crypt.Md5]
                     [goog.crypt.Sha1]
                     [goog.crypt.Sha256])
     :clj (:require [clj-commons.digest :as digest])))

#?(:cljs
   (defn- digest-string [h ^String s]
     (.update h (crypt/stringToUtf8ByteArray s))
     (crypt/byteArrayToHex (.digest h))))

(defn string->md5
  "Calculates MD5 hex digest. Note: MD5 is intended for non-security checksums."
  [s]
  #?(:clj (digest/md5 s)
     :cljs (digest-string (goog.crypt.Md5.) s)))

(defn string->sha1
  "Calculates SHA-1 hex digest. Note: SHA-1 is intended for non-security checksums."
  [s]
  #?(:clj (digest/sha1 s)
     :cljs (digest-string (goog.crypt.Sha1.) s)))

(defn string->sha256
  "Calculates SHA-256 hex digest."
  [s]
  #?(:clj (digest/sha256 s)
     :cljs (digest-string (goog.crypt.Sha256.) s)))

