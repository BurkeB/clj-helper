;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.filter
  "Utility functions for filtering maps and sequences."
  (:require [clojure.string :as str]))

;; Case-insensitive substring search
(defn contains-ci?
  "Returns true if s contains sub, case-insensitive."
  [s sub]
  (when (and (string? s) (string? sub))
    (str/includes? (str/lower-case s)
                   (str/lower-case sub))))

(def searchfilter
  "Alias for contains-ci? for backward compatibility."
  contains-ci?)

;; Check if any of the given keys in m match sub (case-insensitive)
(defn any-key-contains?
  "Returns true if any value under ks in map m contains sub (case-insensitive)."
  [m sub ks]
  (some #(contains-ci? (get m %) sub) ks))

(def keyfilter
  "Alias for any-key-contains? for backward compatibility."
  any-key-contains?)

;; Multi-attribute filter
(defn multi-filter?
  "Determines if map m satisfies all filters in attrs.
   Supported keys:
     :all       -> search sub across all fields
     :blacklist -> exclude maps whose :id is in blacklist set
     any other  -> exact match on value
   Values of nil, \"*\", or :any are treated as wildcards (always true)."
  [m attrs]
  (every?
   (fn [[k v]]
     (cond
       (or (nil? v) (= v "*") (= v :any))
       true

       (= k :all)
       (any-key-contains? m v (keys m))

       (= k :blacklist)
       (not (contains? (set (:blacklist attrs)) (:id m)))

       :else
       (= (get m k) v)))
   attrs))

(def multifilter
  "Alias for multi-filter? for backward compatibility."
  multi-filter?)

;; Filter a sequence of maps by attrs
(defn filter-by-attrs
  "Filters a seq of maps, returning only those where multi-filter? returns true."
  [coll attrs]
  (filter #(multi-filter? % attrs) coll))
