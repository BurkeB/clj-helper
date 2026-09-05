;; Copyright © 2020-2026 FH Münster and contributors
;; Author: Bruno Burke <burke@fh-muenster.de>
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.edn
  (:require [clojure.string :as string]
            #?(:clj [clojure.edn :as edn]
               :cljs [cljs.reader :as edn])
            #?(:clj [clojure.pprint :as pprint]
               :cljs [cljs.pprint :as pprint])))


(defn serialize-edn [edn-data]
  (with-out-str (pprint/pprint edn-data)))

(defn deserialize-edn [ednstr]
  (edn/read-string ednstr))

#?(:cljs
   (defn clj->json [object]
     (js/JSON.stringify (clj->js object) nil 2)))
