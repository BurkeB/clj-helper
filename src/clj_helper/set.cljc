;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.set)

(defn toggle
  "Add or remove item 'value' from set 's'"
  [s value]
  (let [s (or s #{})
        update-fn (if (contains? s value) disj conj)]
    (update-fn s value)))
