;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.string
  (:require [clojure.string :as string]
            #?(:clj [clojure.edn :as edn]
               :cljs [cljs.reader :as edn]))
  #?(:clj (:import [java.time Instant]
                   [java.security SecureRandom])))

(def alphanumeric-chars
  (vec "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"))

#?(:clj (defonce ^:private secure-random (SecureRandom.)))

#?(:clj
   (defn- secure-random-index [^long n]
     (.nextInt ^SecureRandom secure-random n))
   :cljs
   (defn- secure-random-index [n]
     (if (and (exists? js/crypto) (exists? js/crypto.getRandomValues))
       (let [arr (js/Uint32Array. 1)]
         (js/crypto.getRandomValues arr)
         (mod (aget arr 0) n))
       (rand-int n))))

(defn str= [a1 a2]
  (= (str a1) (str a2)))

(defn set->str [set]
  (pr-str set))

(defn str->set [str]
  (edn/read-string str))

(defn shorten [string n]
  (when string
    (subs string 0 (min n (count string)))))

(defn safe-name [val]
  (if (keyword? val)
    (name val)
    (if (string? val)
      val
      "")))

(defn get-random-code
  "Generates pseudo-random alphanumeric code of given length.
   Note: uses standard pseudo-random number generation (non-cryptographic)."
  [length]
  (apply str (repeatedly length #(rand-nth alphanumeric-chars))))

(defn get-secure-random-code
  "Generates cryptographically secure alphanumeric code of given length."
  [length]
  (apply str (repeatedly length #(nth alphanumeric-chars (secure-random-index (count alphanumeric-chars))))))


(defn get-unique-id [prefix]
  (let [now #?(:cljs (.toISOString (new js/Date))
               :clj (.toString (Instant/now)))
        code (str (gensym (shorten prefix 4)))]
    (shorten (str
              #?(:cljs (.join (.split (.substr now 0 19) ":") "-")
                 :clj (-> now
                          (shorten 19)
                          (string/replace #"(-|\:)" ""))) "-" code)
             32)))

(defn date->str [date]
  (try
    (.toLocaleString date)
    (catch
     #?(:cljs js/Error
        :clj Exception) e
      (str date))))


(defn parse-int [s]
  #?(:clj (try
            (Integer/parseInt s)
            (catch Exception _
              nil))
     :cljs (let [v (js/parseInt s)]
             (if (js/isNaN v)
               nil
               v))))



(defn quote-text [text]
  (str "»" text "«"))


(defn parameterstring [data & {:keys [assignment separation quotation]
                               :or {assignment "="
                                    separation ", "
                                    quotation "\""}}]
  (string/join separation
               (map (fn [[key value]]
                      (str (name key) assignment quotation value quotation)) data)))
