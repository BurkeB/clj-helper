;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.filter
  (:require [clojure.string :refer [includes? lower-case]]))

(defn searchfilter [string substring]
  (when (and (string? string) (string? substring))
    (includes? (lower-case string) (lower-case substring))))

(defn keyfilter [map substring keys]
  (some #(searchfilter (get map %) substring) keys))

(defn multifilter [map attributes]
  (every?
   (fn [[key value]]
     (if (or (nil? value) (= "*" value) (= :any value))
       map
       (case key
         :all (keyfilter map value (keys map))
         :blacklist (not ((set (:blacklist attributes)) (:id map)))
         (= (get map key) value))))
   attributes))
