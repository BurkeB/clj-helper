;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.map
  "Utility functions for common map operations.")

(defn update-values
  "Applies ufn to each value in umap, returning a map with the updated values."
  [umap ufn]
  (into {}
        (map
         (juxt first (comp ufn last))
         umap)))

(defn ->getter
  "Returns an extractor function for k:
   - a function: applied directly to the map
   - a sequential path: looked up via get-in
   - any other key: looked up via get"
  [k]
  (cond
    (fn? k)         k
    (sequential? k) #(get-in % k)
    :else           #(get % k)))

(defn key-equals?
  "Returns true if the value at key k in map m equals val.
   Supports nested lookup when k is a sequential collection / vector (via get-in) or a function."
  [m k val]
  (= ((->getter k) m) val))

(def keyqual
  "Alias for key-equals? for backward compatibility."
  key-equals?)
