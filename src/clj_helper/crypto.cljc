;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.crypto
  "Cross-platform cryptographic hash utilities."
  #?(:cljs (:require [goog.crypt :as crypt]
                     [goog.crypt.Md5]
                     [goog.crypt.Sha1]
                     [goog.crypt.Sha256]
                     [goog.crypt.base64 :as b64])
     :clj (:require [clj-commons.digest :as digest]))
  #?(:clj (:import [java.nio.charset StandardCharsets]
                   [java.util Base64])))

#?(:cljs
   (defn- digest-string [h ^String s]
     (.update h (crypt/stringToUtf8ByteArray s))
     (crypt/byteArrayToHex (.digest h))))

(defn md5
  "Calculates MD5 hex digest. Note: MD5 is intended for non-security checksums."
  [s]
  #?(:clj (digest/md5 s)
     :cljs (digest-string (goog.crypt.Md5.) s)))

(def string->md5
  "Alias for md5 for backward compatibility."
  md5)

(defn sha1
  "Calculates SHA-1 hex digest. Note: SHA-1 is intended for non-security checksums."
  [s]
  #?(:clj (digest/sha1 s)
     :cljs (digest-string (goog.crypt.Sha1.) s)))

(def string->sha1
  "Alias for sha1 for backward compatibility."
  sha1)

(defn sha256
  "Calculates SHA-256 hex digest."
  [s]
  #?(:clj (digest/sha256 s)
     :cljs (digest-string (goog.crypt.Sha256.) s)))

(def string->sha256
  "Alias for sha256 for backward compatibility."
  sha256)

(defn base64-encode
  "Encodes a string to Base64 using standard alphabet."
  [s]
  #?(:cljs (b64/encodeString s)
     :clj  (let [bytes (.getBytes (str s) StandardCharsets/UTF_8)]
             (.encodeToString (Base64/getEncoder) bytes))))
