;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
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

(def eq?
  "Alias for str= for concise string-equality checking."
  str=)

(defn set->str [set]
  (pr-str set))

(defn str->set [str]
  (edn/read-string str))

(defn shorten [string n]
  (when string
    (subs string 0 (min n (count string)))))

(defn safe-name [val]
  (cond
    (keyword? val) (name val)
    (string? val)  val
    :else          ""))

(defn get-random-code
  "Generates pseudo-random alphanumeric code of given length.
   Note: uses standard pseudo-random number generation (non-cryptographic)."
  [length]
  (apply str (repeatedly length #(rand-nth alphanumeric-chars))))

(def rand-code
  "Alias for get-random-code."
  get-random-code)

(defn get-secure-random-code
  "Generates cryptographically secure alphanumeric code of given length."
  [length]
  (apply str (repeatedly length #(nth alphanumeric-chars (secure-random-index (count alphanumeric-chars))))))

(defn unique-id
  "Generates a near-UUID unique identifier by combining a millisecond timestamp and gensym, with an optional tag/postfix."
  ([]
   (unique-id nil))
  ([tag]
   (let [now #?(:cljs (.toISOString (new js/Date))
                :clj (.toString (Instant/now)))
         timestamp (shorten (string/replace now #"[-:.]" "") 22)
         uniq-part (-> (gensym)
                       name
                       (string/replace #"__" ""))
         base      (str timestamp uniq-part
                        (when tag (str "-" tag)))]
     (shorten base 32))))

(def get-unique-id
  "Alias for unique-id for backward compatibility."
  unique-id)

(defn date->str [date]
  (try
    #?(:cljs (.toLocaleString date)
       :clj  (.toString ^Object date))
    (catch
     #?(:cljs js/Error
        :clj Exception) _
      (str date))))

(defn parse-int
  "Parses a string into an integer, returns nil on failure."
  [s]
  (when (string? s)
    (try
      #?(:clj (Integer/parseInt s)
         :cljs (let [res (js/parseInt s)]
                 (if (js/isNaN res) nil res)))
      (catch #?(:clj Exception :cljs :default) _
        nil))))

(defn quote-text [text]
  (str "»" text "«"))

(defn param-string
  "Builds a string of key and quoted value pairs from a map with configurable separators and assignment symbol.
   Supports both :assign / :sep / :quote and legacy :assignment / :separation / :quotation."
  [m & {:keys [assign sep quote assignment separation quotation]
        :or {assign "=" sep ", " quote "\""}}]
  (let [as (or assignment assign)
        sp (or separation sep)
        qu (or quotation quote)]
    (string/join sp
                 (map (fn [[k v]]
                        (str (name k) as qu v qu))
                      m))))

(def parameterstring
  "Alias for param-string for backward compatibility."
  param-string)
