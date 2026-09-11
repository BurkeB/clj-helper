;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.compression
  (:require [clojure.java.io :as io]
            [clj-helper.binary :refer [byte-array->base64 base64->byte-array]])
  (:import [org.tukaani.xz XZInputStream XZOutputStream LZMA2Options]
           [java.io ByteArrayInputStream ByteArrayOutputStream]
           [java.nio.charset StandardCharsets]))

(def ^:dynamic *max-decompression-bytes*
  "Default maximum bytes allowed during decompression to prevent zip/xz bombs (16 MB)."
  (* 16 1024 1024))

(defn string->xz
  "Compresses a UTF-8 string into an XZ-compressed byte array."
  [^String string]
  (let [input-bytes (.getBytes string StandardCharsets/UTF_8)
        baos (ByteArrayOutputStream.)]
    (with-open [xzos (XZOutputStream. baos (LZMA2Options.))]
      (.write xzos input-bytes))
    (.toByteArray baos)))

(defn xz->string
  "Decompresses an XZ byte array into a UTF-8 string.
   Optionally accepts max-bytes limit to protect against decompression bombs."
  ([^bytes xzdata]
   (xz->string xzdata *max-decompression-bytes*))
  ([^bytes xzdata max-bytes]
   (let [baos (ByteArrayOutputStream.)
         buf (byte-array 8192)]
     (with-open [xzis (XZInputStream. (ByteArrayInputStream. xzdata))]
       (loop [total 0]
         (let [n (.read xzis buf)]
           (when (pos? n)
             (let [new-total (+ total n)]
               (when (and max-bytes (> new-total max-bytes))
                 (throw (ex-info "Decompression size exceeds maximum limit"
                                 {:max-bytes max-bytes :bytes-read new-total})))
               (.write baos buf 0 n)
               (recur new-total))))))
     (String. (.toByteArray baos) StandardCharsets/UTF_8))))

(defn string->compressed
  "Compresses string to compressed byte array using specified compressor (default: 'xz')."
  ([string]
   (string->xz string))
  ([string compressor]
   (if (= compressor "xz")
     (string->xz string)
     (throw (IllegalArgumentException. (str "Unsupported compressor: " compressor ". Only 'xz' is supported."))))))

(defn compressed->string
  "Decompresses byte array to string using specified compressor (default: 'xz')."
  ([compressed-data]
   (xz->string compressed-data))
  ([compressed-data compressor]
   (if (= compressor "xz")
     (xz->string compressed-data)
     (throw (IllegalArgumentException. (str "Unsupported compressor: " compressor ". Only 'xz' is supported."))))))

(defn string->xz->base64 [string]
  (byte-array->base64 (string->xz string)))

(defn base64->xz->string [b64xz]
  (xz->string (base64->byte-array b64xz)))



