;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.i18n
  "A simple i18n utility for Clojure(Script).
   Treats translations as {:i18n true :en \"...\"} maps."
  (:require [clojure.string :as str]))

;; -----------------------------------------------------------------------------
;; 1. State & Configuration
;; -----------------------------------------------------------------------------

(defonce ^:private config
  (atom {:default-lang :en
         :fallback-order [:en] ;; If target lang missing, try these in order
         :resolvers []}))      ;; Vector of (fn [] -> keyword)

(def ^:dynamic *current-lang*
  "Dynamic variable to force a language for a specific scope.
   Useful for backend request handling (SSR) or testing."
  nil)

;; -----------------------------------------------------------------------------
;; 2. Registry & Setup API
;; -----------------------------------------------------------------------------

(defn set-default-lang!
  "Sets the ultimate fallback language if detection fails."
  [lang-key]
  (swap! config assoc :default-lang (keyword lang-key)))

(defn set-fallback-order!
  "Sets the order of languages to try if the requested language is missing
   in the i18n map. E.g. [:en :de]"
  [lang-keys]
  (swap! config assoc :fallback-order (mapv keyword lang-keys)))

(defn register-resolver!
  "Adds a detector function. The function should take no args and return
   a language keyword (e.g. :en) or nil.
   Resolvers are tried in order of registration."
  [f]
  (swap! config update :resolvers conj f))

(defn clear-resolvers!
  "Clears all registered language resolvers."
  []
  (swap! config assoc :resolvers []))

;; -----------------------------------------------------------------------------
;; 3. Internal Helpers
;; -----------------------------------------------------------------------------

(defn normalize
  "Converts input to keyword representation of language."
  [l]
  (when l (keyword l)))

(defn- get-derived-langs
  "Given :de-AT, returns [:de-AT :de].
   Useful for trying region specific first, then base language."
  [lang]
  (let [l-str (name lang)
        parts (str/split l-str #"-")]
    (if (> (count parts) 1)
      [lang (keyword (first parts))]
      [lang])))

(defn- resolve-lang
  "Determines the current active language based on:
   1. Dynamic scope (*current-lang*)
   2. Registered resolvers
   3. Configured default"
  []
  (or *current-lang*
      (some (fn [resolver] (normalize (resolver))) (:resolvers @config))
      (:default-lang @config)))

(defn- lookup-fallback
  "The core search algorithm.
   1. Tries target language (and base, e.g., :de-AT -> :de)
   2. Tries global fallback order (e.g., :en)
   3. Returns the first found value or nil."
  [i18n-map target-lang]
  (let [global-fallbacks (:fallback-order @config)
        ;; Combine specific target candidates + global fallbacks
        ;; e.g. target :de-AT => [:de-AT :de :en]
        candidates (distinct (concat (get-derived-langs target-lang)
                                     global-fallbacks))]
    (some #(get i18n-map %) candidates)))

;; -----------------------------------------------------------------------------
;; 4. Core Public API
;; -----------------------------------------------------------------------------

(defn i18n-map?
  "Checks if x is a valid i18n map."
  [x]
  (and (map? x) (true? (:i18n x))))

(defn available-langs
  "Returns available language keywords from an i18n map."
  [i18n-map]
  (keys (dissoc i18n-map :i18n)))

(defn i18n-get
  "Resolves the string value for the given data.
   - If data is a string: returns it as-is.
   - If data is an i18n-map: looks up value for lang (or current-lang).
   - If value not found for lang: tries fallbacks.
   - If still not found: returns missing-placeholder or empty string."
  ([data]
   (i18n-get data (resolve-lang)))
  ([data lang-or-opts]
   (let [lang (if (map? lang-or-opts) (:lang lang-or-opts) lang-or-opts) ;; Support options map if needed
         target (normalize lang)]
     (cond
       (string? data) data
       (nil? data)    nil
       (i18n-map? data) (or (lookup-fallback data target) "")
       :else (str data)))))

(defn i18n-assoc
  "Updates or creates an i18n map.
   If `x` is a string, it converts it to an i18n map first,
   preserving the string as the value for the *current* default language,
   and then adding the new lang/value."
  [x lang value]
  (let [k-lang (keyword lang)]
    (if (i18n-map? x)
      (assoc x k-lang value)
      ;; Use default lang for the original string if promoting string->map
      (assoc {:i18n true}
             (:default-lang @config) x
             k-lang value))))

(defn i18n-set
  "Creates a fresh i18n map with a single entry."
  [lang value]
  {:i18n true (keyword lang) value})

;; -----------------------------------------------------------------------------
;; 5. Macros & Context
;; -----------------------------------------------------------------------------

(defmacro with-lang
  "Executes body with the language forced to `lang`.
   Useful for user-specific server-side rendering or generating emails."
  [lang & body]
  `(binding [*current-lang* (normalize ~lang)]
     ~@body))
