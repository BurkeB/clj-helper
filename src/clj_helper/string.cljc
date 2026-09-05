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
  #?(:clj (:import [java.time Instant])))

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

(defn get-random-code [length]
  (let [chars (string/split "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" #"")]
    (apply str (take length (repeatedly #(rand-nth chars))))))

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
            (catch Exception e
              nil))
     :cljs (try
             (js/parseInt s)
             (catch :default e
               nil))))


(defn quote-text [text]
  (str "»" text "«"))


(defn parameterstring [data & {:keys [assignment separation quotation]
                               :or {assignment "="
                                    separation ", "
                                    quotation "\""}}]
  (string/join separation
               (map (fn [[key value]]
                      (str (name key) assignment quotation value quotation)) data)))
