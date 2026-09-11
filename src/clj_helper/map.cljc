;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.map)

(defn update-values [umap ufn]
  (into {}
        (map
         (juxt first (comp ufn last))
         umap)))

(defn keyqual
  "Checks if a key in a map is equal to a given value.
   If key is a vector the value is retrieved by 'get-in', otherwise by 'get'."
  [map key val]
  (if (sequential? key)
    (= (get-in map key) val)
    (= (get map key) val)))

