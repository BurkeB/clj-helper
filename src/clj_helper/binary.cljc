;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.binary
  "Cross-platform utilities for binary and Base64 conversions."
  #?(:clj (:import [java.util Base64]
                   [java.nio.charset StandardCharsets]
                   [java.nio ByteBuffer]))
  #?(:cljs (:require [goog.crypt :as crypt]
                     [goog.crypt.base64 :as b64])))

(defn byte-array->string
  "Decodes UTF-8 byte array to string."
  [bytearray]
  (when bytearray
    #?(:clj (String. ^bytes bytearray StandardCharsets/UTF_8)
       :cljs (crypt/utf8ByteArrayToString (js/Array.from bytearray)))))

(def bytes->string
  "Alias for byte-array->string."
  byte-array->string)

(defn string->byte-array
  "Converts string to UTF-8 byte array."
  [^String string]
  (when string
    #?(:clj (.getBytes string StandardCharsets/UTF_8)
       :cljs (crypt/stringToUtf8ByteArray string))))

(def string->bytes
  "Alias for string->byte-array."
  string->byte-array)

#?(:clj
   (defn byte-buffer->byte-array
     "Extracts byte array from ByteBuffer."
     [^ByteBuffer buffer]
     (if (.hasArray buffer)
       (.array buffer)
       (let [bytes (byte-array (.remaining buffer))]
         (.get buffer bytes)
         bytes))))

(defn byte-array->base64
  "Encodes byte array to Base64 string."
  [byte-array]
  (when byte-array
    #?(:clj (.encodeToString (Base64/getEncoder) ^bytes byte-array)
       :cljs (b64/encodeByteArray (js/Array.from byte-array)))))

(def bytes->base64
  "Alias for byte-array->base64."
  byte-array->base64)

#?(:clj
   (defn byte-buffer->base64
     "Encodes ByteBuffer to Base64 string."
     [^ByteBuffer buffer]
     (-> buffer
         byte-buffer->byte-array
         byte-array->base64)))

(defn base64->byte-array
  "Decodes Base64 string to byte array."
  [base64-string]
  (when base64-string
    #?(:clj (.decode (Base64/getDecoder) (str base64-string))
       :cljs (js/Uint8Array. (b64/decodeStringToByteArray (str base64-string))))))

(def base64->bytes
  "Alias for base64->byte-array."
  base64->byte-array)

(defn string->base64
  "Encodes string to Base64 string."
  [string]
  (when string
    (-> string
        string->byte-array
        byte-array->base64)))

(defn base64->string
  "Decodes Base64 string to string."
  [base64]
  (when base64
    (-> base64
        base64->byte-array
        byte-array->string)))
