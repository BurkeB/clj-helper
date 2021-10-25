(ns clj-helper.compression
  (:require [clojure.test :refer :all]
            [clojure.java.io :refer [file output-stream input-stream] :as io]
            [clj-compress.core :refer [compress-data decompress-data]]
            [clj-helper.binary :refer [byte-array->base64 base64->string
                                       base64->byte-array]]
            )
  (:import (java.io ByteArrayOutputStream)
           (org.apache.commons.io FileUtils)))

(defn string->compressed [string compressor]
  (let [s string
        sbuf (.getBytes s)
        cbuf        (ByteArrayOutputStream.)
        comp-size   (compress-data sbuf cbuf compressor)]
    (.toByteArray cbuf)))

(defn string->xz [string]
  (string->compressed string "xz"))

(defn compressed->string [compressed-data compressor]
  (let [coutbuf (ByteArrayOutputStream.)
        decomp-size (decompress-data compressed-data coutbuf compressor)]
    (.toString coutbuf)))

(defn xz->string [xzdata]
  (compressed->string xzdata "xz"))

(defn string->xz->base64 [string]
  (byte-array->base64 (string->xz string)))

(defn base64->xz->string [b64xz]
  (xz->string (base64->byte-array b64xz)))


