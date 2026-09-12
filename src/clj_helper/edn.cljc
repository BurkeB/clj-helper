;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.edn
  "Utilities for EDN serialization and deserialization (and JSON for ClojureScript)."
  (:require #?(:clj [clojure.edn :as edn]
               :cljs [cljs.reader :as edn])
            #?(:clj [clojure.pprint :as pprint]
               :cljs [cljs.pprint :as pprint])))

(defn edn->str
  "Serializes Clojure data to a pretty-printed EDN string."
  [data]
  (binding [pprint/*print-right-margin* 80]
    (with-out-str
      (pprint/pprint data))))

(def serialize-edn
  "Alias for edn->str for backward compatibility."
  edn->str)

(defn str->edn
  "Parses an EDN-formatted string into Clojure data."
  [s]
  (edn/read-string s))

(def deserialize-edn
  "Alias for str->edn for backward compatibility."
  str->edn)

#?(:cljs
   (defn clj->json
     "Converts ClojureScript data structures to a JSON string with indentation."
     [object]
     (js/JSON.stringify (clj->js object) nil 2)))
