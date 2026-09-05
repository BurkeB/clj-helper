;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.binary
  #?(:clj (:import [java.time LocalDateTime]
                   [java.util Base64])))


(defn byte-array->string [bytearray]
  (apply str
         (map char bytearray)))

(defn string->byte-array [string]
  (byte-array (map byte string)))


#?(:clj
   (defn byte-buffer->byte-array [Buffer]
       (.array Buffer)))

#?(:clj
   (defn byte-array->base64 [byte-array]
     (let [encoder (Base64/getEncoder)]
       (byte-array->string (.encode encoder byte-array)))))

#?(:clj
   (defn byte-buffer->base64 [Buffer]
     (-> Buffer
         byte-buffer->byte-array
         byte-array->base64)))


#?(:clj
   (defn base64->byte-array [base64-string]
     (let [decoder (Base64/getDecoder)]
       (.decode decoder (.toString base64-string))
       )))



#?(:clj
   (defn string->base64 [string]
     (-> string
         string->byte-array
         byte-array->base64)))

#?(:clj
   (defn base64->string [base64]
     (-> base64
         base64->byte-array
         byte-array->string)))


