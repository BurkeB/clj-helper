;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.i18n
  "A cross-platform i18n utility for Clojure(Script).
   Treats translations as {:i18n true :en \"...\" :de \"...\"} maps,
   supports global and scoped dictionaries, regional fallbacks, custom
   resolvers (e.g. Reagent atoms), and single-pass string interpolation
   (%s, %1$s, {0}, {key}) via `tr` and `format-str`."
  (:require [clojure.string :as str])
  #?(:cljs (:require-macros [clj-helper.i18n :refer [with-lang]])))

;; -----------------------------------------------------------------------------
;; 1. State & Configuration
;; -----------------------------------------------------------------------------

(defonce ^:private config
  (atom {:default-lang :en
         :fallback-order [:en] ;; If target lang missing, try these in order
         :resolvers []         ;; Vector of (fn [] -> keyword)
         :dictionary {}}))     ;; Map of keyword/string -> i18n-map

(def ^:dynamic *current-lang*
  "Dynamic variable to force a language for a specific scope.
   Useful for backend request handling (SSR) or testing."
  nil)

(def ^:private metadata-keys
  "Non-language keys allowed inside an i18n map."
  #{:i18n :explanation :description :doc :comment})

;; -----------------------------------------------------------------------------
;; 2. Internal Helpers
;; -----------------------------------------------------------------------------

(defn normalize
  "Converts input (keyword, string, symbol) to a language keyword.
   Returns nil for nil or blank strings."
  [l]
  (when l
    (let [s (str/trim (if (or (keyword? l) (symbol? l)) (name l) (str l)))]
      (when-not (str/blank? s)
        (keyword s)))))

(defn- get-derived-langs
  "Given a language keyword such as :de-AT, :de_AT, or :DE, returns a vector
   of candidate language keywords from most specific to base language, e.g.:
   :de-AT -> [:de-AT :de-at :de]
   :de_AT -> [:de_AT :de-AT :de-at :de]
   :DE    -> [:DE :de]"
  [lang]
  (if-let [norm (normalize lang)]
    (let [raw (name norm)
          hyphenated (str/replace raw "_" "-")
          parts (str/split hyphenated #"-")
          base-lower (str/lower-case (first parts))]
      (if (> (count parts) 1)
        (let [canonical (str base-lower "-" (str/upper-case (second parts)))
              lower-full (str/lower-case hyphenated)]
          (vec (distinct [(keyword raw)
                          (keyword hyphenated)
                          (keyword canonical)
                          (keyword lower-full)
                          (keyword base-lower)])))
        (vec (distinct [(keyword raw)
                        (keyword base-lower)]))))
    []))

(defn resolve-lang
  "Determines the current active language based on:
   1. Dynamic scope (*current-lang*)
   2. Registered resolvers (ignoring any resolver that throws or returns nil)
   3. Configured default"
  []
  (or (normalize *current-lang*)
      (some (fn [resolver]
              (try
                (normalize (resolver))
                (catch #?(:clj Exception :cljs :default) _
                  nil)))
            (:resolvers @config))
      (:default-lang @config)))

(def current-lang
  "Alias for `resolve-lang`. Returns the currently active language keyword."
  resolve-lang)

(defn- lookup-fallback
  "The core search algorithm.
   1. Tries target language (and derived variants, e.g., :de_AT -> :de-AT -> :de)
   2. Tries global fallback order (e.g., :en)
   3. Returns the first found value or nil."
  [i18n-map target-lang]
  (when (map? i18n-map)
    (let [global-fallbacks (:fallback-order @config)
          candidates (distinct (concat (get-derived-langs target-lang)
                                       (mapcat get-derived-langs global-fallbacks)))]
      (some #(get i18n-map %) candidates))))

(defn- promote-dict-entry
  "Ensures map entries registered in a dictionary carry `:i18n true`."
  [v]
  (if (map? v)
    (assoc v :i18n true)
    v))

(defn- normalize-dictionary [dict-map]
  (reduce-kv (fn [acc k v]
               (assoc acc k (promote-dict-entry v)))
             {}
             (or dict-map {})))

(defn- lookup-dict-entry
  "Looks up key `k` in `dict`, trying `k` directly first, then keyword/string
   equivalents so dictionaries keyed by strings or keywords work interchangeably."
  [dict k]
  (when (and (map? dict) (some? k))
    (or (get dict k)
        (when (keyword? k) (get dict (name k)))
        (when (string? k) (get dict (keyword k))))))

;; -----------------------------------------------------------------------------
;; 3. Registry, Dictionary & Setup API
;; -----------------------------------------------------------------------------

(defn set-default-lang!
  "Sets the ultimate fallback language if detection fails."
  [lang-key]
  (swap! config assoc :default-lang (normalize lang-key)))

(defn set-fallback-order!
  "Sets the order of languages to try if the requested language is missing
   in the i18n map. E.g. [:en :de]"
  [lang-keys]
  (swap! config assoc :fallback-order (vec (keep normalize lang-keys))))

(defn register-resolver!
  "Adds a detector function. The function should take no args and return
   a language keyword (e.g. :en) or nil.
   Resolvers are tried in order of registration. Duplicate function references
   are ignored so hot-reloading does not accumulate duplicate resolvers."
  [f]
  (when (fn? f)
    (swap! config update :resolvers
           (fn [rs]
             (if (some #{f} rs)
               rs
               (conj rs f))))))

(defn clear-resolvers!
  "Clears all registered language resolvers."
  []
  (swap! config assoc :resolvers []))

(defn set-dictionary!
  "Replaces the global translation dictionary with `dict-map`.
   Map entries in `dict-map` automatically have `:i18n true` attached."
  [dict-map]
  (swap! config assoc :dictionary (normalize-dictionary dict-map)))

(defn register-dictionary!
  "Merges `dict-map` into the global translation dictionary.
   Can be called multiple times from different modules to register
   domain-specific translation keys."
  [dict-map]
  (swap! config update :dictionary merge (normalize-dictionary dict-map)))

(defn clear-dictionary!
  "Clears all entries from the global translation dictionary."
  []
  (swap! config assoc :dictionary {}))

(defn get-dictionary
  "Returns the currently registered translation dictionary."
  []
  (:dictionary @config))

;; -----------------------------------------------------------------------------
;; 4. String Interpolation & Formatting
;; -----------------------------------------------------------------------------

(def ^:private placeholder-pattern
  "Matches:
   - `%s` (sequential placeholder)
   - `%1$s`, `%2$s` (1-based positional printf-style placeholder)
   - `{0}`, `{1}` (0-based positional placeholder)
   - `{key}` (named map placeholder)"
  #"%(?:\d+\$)?s|\{[a-zA-Z0-9_-]+\}")

(defn format-str
  "Single-pass, cross-platform string interpolation supporting:
   - Sequential `%s`: `(format-str \"Hello %s!\" \"World\")`
   - Positional `%1$s`, `%2$s` (1-based): `(format-str \"%2$s before %1$s\" \"A\" \"B\")`
   - Positional `{0}`, `{1}` (0-based): `(format-str \"{1} before {0}\" \"A\" \"B\")`
   - Named `{key}` (when first arg is a map): `(format-str \"Hi {name}\" {:name \"Ada\"})`

   Because replacement runs in a single regex pass using a function callback,
   replacement values containing `%s`, `$`, or `\\` are never re-interpolated
   or corrupted in either Clojure or ClojureScript."
  [template & args]
  (let [s (str template)]
    (if (empty? args)
      s
      (let [arg-vec (vec args)
            arg-count (count arg-vec)
            first-arg (first arg-vec)
            named-map (when (map? first-arg) first-arg)
            seq-idx (atom 0)]
        (str/replace
         s
         placeholder-pattern
         (fn [match]
           (cond
             ;; 1. Sequential %s
             (= match "%s")
             (let [idx @seq-idx]
               (swap! seq-idx inc)
               (if (< idx arg-count)
                 (str (nth arg-vec idx))
                 match))

             ;; 2. 1-based positional %1$s
             (str/starts-with? match "%")
             (let [num-str (subs match 1 (- (count match) 2))
                   idx (some-> (parse-long num-str) dec)]
               (if (and idx (>= idx 0) (< idx arg-count))
                 (str (nth arg-vec idx))
                 match))

             ;; 3. Brace placeholder {key} or {0}
             (str/starts-with? match "{")
             (let [k (subs match 1 (dec (count match)))
                   kw (keyword k)]
               (cond
                 (and named-map (contains? named-map kw))
                 (str (get named-map kw))

                 (and named-map (contains? named-map k))
                 (str (get named-map k))

                 (re-matches #"\d+" k)
                 (let [idx (parse-long k)]
                   (if (and idx (>= idx 0) (< idx arg-count))
                     (str (nth arg-vec idx))
                     match))

                 :else match))

             :else match)))))))

;; -----------------------------------------------------------------------------
;; 5. Core Public API
;; -----------------------------------------------------------------------------

(defn i18n-map?
  "Checks if x is a valid i18n map."
  [x]
  (and (map? x) (true? (:i18n x))))

(defn available-langs
  "Returns available language keywords from an i18n map, excluding metadata keys."
  [i18n-map]
  (when (map? i18n-map)
    (keys (apply dissoc i18n-map metadata-keys))))

(defn i18n-get
  "Resolves the string value for the given data.
   - If data is a keyword: looks it up in the registered dictionary (or `:dictionary` in opts),
     falling back to `:default` in opts or `(name data)` if not found.
   - If data is an i18n-map: looks up value for `lang` (or current active lang) with fallback order.
   - If data is a string: returns it as-is.
   - If data is nil: returns nil.
   - Supports `:args` in `lang-or-opts` map for formatting."
  ([data]
   (i18n-get data (resolve-lang)))
  ([data lang-or-opts]
   (let [opts (when (map? lang-or-opts) lang-or-opts)
         lang (if opts (or (:lang opts) (resolve-lang)) lang-or-opts)
         target (normalize lang)
         dict (or (:dictionary opts) (:dictionary @config))
         default-val (:default opts)
         fmt-args (:args opts)
         resolved (cond
                    (string? data) data
                    (nil? data)    nil
                    (keyword? data)
                    (if-let [entry (lookup-dict-entry dict data)]
                      (if (map? entry)
                        (or (lookup-fallback entry target) default-val "")
                        (str entry))
                      (or default-val (name data)))
                    (i18n-map? data) (or (lookup-fallback data target) default-val "")
                    :else (str data))]
     (if (and (string? resolved) (seq fmt-args))
       (apply format-str resolved fmt-args)
       resolved))))

(defn tr
  "Translates `data` (a keyword from the registered dictionary, an i18n map, or a string)
   into the currently active language, optionally interpolating format arguments
   (`%s`, `%1$s`, `{0}`, or `{key}`).
   Returns an empty string for `nil`."
  ([data]
   (if (nil? data)
     ""
     (or (i18n-get data) "")))
  ([data & format-args]
   (let [s (tr data)]
     (if (seq format-args)
       (apply format-str s format-args)
       s))))

(defn tr-in
  "Looks up `k` (a keyword or string) in a specific `dict` map and translates it
   using the active language (even if entries in `dict` omit `:i18n true`).
   If not found in `dict`, falls back to `(tr k)`."
  ([dict k]
   (if-let [entry (lookup-dict-entry dict k)]
     (if (map? entry)
       (or (lookup-fallback entry (resolve-lang)) "")
       (str entry))
     (tr k)))
  ([dict k & format-args]
   (let [s (tr-in dict k)]
     (if (seq format-args)
       (apply format-str s format-args)
       s))))

(defn i18n-assoc
  "Updates or creates an i18n map.
   If `x` is a string, it converts it to an i18n map first,
   preserving the string as the value for the *current* default language,
   and then adding the new lang/value."
  [x lang value]
  (let [k-lang (normalize lang)]
    (if (i18n-map? x)
      (assoc x k-lang value)
      ;; Use default lang for the original string if promoting string->map
      (assoc {:i18n true}
             (:default-lang @config) x
             k-lang value))))

(defn i18n-set
  "Creates a fresh i18n map with a single entry."
  [lang value]
  {:i18n true (normalize lang) value})

;; -----------------------------------------------------------------------------
;; 6. Macros & Context
;; -----------------------------------------------------------------------------

#?(:clj
   (defmacro with-lang
     "Executes body with the language forced to `lang`.
      Useful for user-specific server-side rendering, testing, or generating emails."
     [lang & body]
     `(binding [*current-lang* (normalize ~lang)]
        ~@body)))
