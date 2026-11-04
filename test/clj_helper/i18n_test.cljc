;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.i18n-test
  (:require [clj-helper.i18n :as i18n]
            [clojure.test :refer [deftest is testing use-fixtures]]))

;; Helper to reset config between tests
(defn reset-config-fixture [f]
  (i18n/set-default-lang! :en)
  (i18n/set-fallback-order! [:en])
  (i18n/clear-resolvers!)
  (i18n/clear-dictionary!)
  (f))

(use-fixtures :each reset-config-fixture)

(deftest configuration-test
  (testing "Default language configuration"
    (i18n/set-default-lang! :de)
    (is (= :de (i18n/resolve-lang)))
    (is (= :de (i18n/current-lang)))
    (is (= "Hallo" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))

    (i18n/set-default-lang! :en)
    (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"}))))

  (testing "Fallback order configuration"
    (i18n/set-default-lang! :fr) ;; Missing in map
    (i18n/set-fallback-order! [:de :en])

    ;; Should fall back to :de first
    (is (= "Hallo" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))

    ;; If :de missing, should fall back to :en
    (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :es "Hola"}))))

  (testing "Resolver registration, deduplication, and error resilience"
    (i18n/set-default-lang! :en)
    (let [es-resolver (constantly :es)]
      (i18n/register-resolver! es-resolver)
      (i18n/register-resolver! es-resolver) ;; Duplicate registration (hot-reload)
      (is (= "Hola" (i18n/i18n-get {:i18n true :en "Hello" :es "Hola"}))))

    ;; Resolver precedence (first registered wins) & exception safety
    (i18n/clear-resolvers!)
    (i18n/register-resolver! (fn [] (throw (ex-info "DOM unavailable" {})))) ;; Throws, should be ignored safely
    (i18n/register-resolver! (constantly nil)) ;; Returns nil, should continue
    (i18n/register-resolver! (constantly :de))
    (is (= "Hallo" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo" :es "Hola"})))))

(deftest locale-normalization-test
  (testing "Normalize and regional/case fallback variants"
    (is (nil? (i18n/normalize nil)))
    (is (nil? (i18n/normalize "   ")))
    (is (= :de (i18n/normalize " de ")))

    ;; Underscore locale ("de_AT") matches :de-AT or :de
    (is (= "Servus" (i18n/i18n-get {:i18n true :de "Hallo" :de-AT "Servus"} "de_AT")))
    (is (= "Hallo" (i18n/i18n-get {:i18n true :de "Hallo" :en "Hello"} "de_DE")))

    ;; Uppercase locale ("DE" or :DE) falls back to :de
    (is (= "Hallo" (i18n/i18n-get {:i18n true :de "Hallo" :en "Hello"} "DE")))
    (is (= "Hallo" (i18n/i18n-get {:i18n true :de "Hallo" :en "Hello"} :DE)))

    ;; Lowercase regional ("de-at") matches canonical :de-AT
    (is (= "Servus" (i18n/i18n-get {:i18n true :de "Hallo" :de-AT "Servus"} "de-at")))))

(deftest core-api-test
  (testing "i18n-map?"
    (is (true? (i18n/i18n-map? {:i18n true :en "Test"})))
    (is (false? (i18n/i18n-map? {:en "Test"})))
    (is (false? (i18n/i18n-map? "Not a map"))))

  (testing "available-langs excludes metadata keys and handles non-maps safely"
    (is (= #{:en :de}
           (set (i18n/available-langs {:i18n true :en "A" :de "B" :explanation "Info" :doc "Doc"}))))
    (is (nil? (i18n/available-langs nil)))
    (is (nil? (i18n/available-langs "raw string"))))

  (testing "i18n-get"
    (testing "String input"
      (is (= "Just a string" (i18n/i18n-get "Just a string"))))

    (testing "Nil input"
      (is (nil? (i18n/i18n-get nil))))

    (testing "Basic lookup"
      (i18n/set-default-lang! :en)
      (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"}))))

    (testing "Region specific lookup"
      ;; :de-AT should fallback to :de if :de-AT missing
      (i18n/set-default-lang! :de-AT)
      (is (= "Hallo" (i18n/i18n-get {:i18n true :de "Hallo" :en "Hello"})))

      ;; :de-AT present
      (is (= "Servus" (i18n/i18n-get {:i18n true :de "Hallo" :de-AT "Servus"}))))

    (testing "Missing value handling"
      (i18n/set-default-lang! :fr)
      (i18n/set-fallback-order! [])
      (is (= "" (i18n/i18n-get {:i18n true :en "Hello"})) "Should return empty string if no translation found")))

  (testing "i18n-assoc"
    (let [m {:i18n true :en "Hello"}]
      (is (= {:i18n true :en "Hello" :de "Hallo"}
             (i18n/i18n-assoc m :de "Hallo"))))

    (testing "Promoting string to i18n map"
      (i18n/set-default-lang! :en)
      (is (= {:i18n true :en "Hello" :de "Hallo"}
             (i18n/i18n-assoc "Hello" :de "Hallo")))))

  (testing "i18n-set"
    (is (= {:i18n true :en "Hello"} (i18n/i18n-set :en "Hello")))))

(deftest dictionary-and-tr-test
  (testing "Dictionary registration (with and without :i18n true) and keyword lookup"
    (i18n/set-default-lang! :de)
    (i18n/set-fallback-order! [:de :en])
    (i18n/register-dictionary!
     {:save {:en "Save" :de "Speichern"} ;; Even without :i18n true!
      :delete-confirm {:i18n true :en "Delete '%s'?" :de "'%s' wirklich löschen?"}})

    (is (true? (i18n/i18n-map? (get (i18n/get-dictionary) :save))))
    (is (= "Speichern" (i18n/i18n-get :save)))
    (is (= "Save" (i18n/i18n-get :save :en)))
    (is (= "Speichern" (i18n/tr :save)))

    ;; Merge additional dictionary entries
    (i18n/register-dictionary!
     {:cancel {:en "Cancel" :de "Abbrechen"}})
    (is (= "Abbrechen" (i18n/tr :cancel)))
    (is (= "Speichern" (i18n/tr :save)))

    ;; Unknown keyword falls back to keyword name (or :default in opts)
    (is (= "unknown-key" (i18n/tr :unknown-key)))
    (is (= "Custom Default" (i18n/i18n-get :unknown-key {:default "Custom Default"}))))

  (testing "Single-pass string interpolation with %s, %1$s, {0}, and {key}"
    (i18n/set-default-lang! :de)
    (i18n/register-dictionary!
     {:delete-confirm {:en "Delete '%s'?" :de "'%s' wirklich löschen?"}
      :stats {:en "%s rows x %s cols" :de "%s Zeilen x %s Spalten"}
      :reordered {:en "Reply to %1$s in %2$s" :de "In %2$s auf %1$s antworten"}
      :indexed {:en "{0} of {1}" :de "{1} enthält {0}"}
      :named {:en "Hello {user}, {count} items" :de "Hallo {user}, {count} Einträge"}})

    (is (= "'report.pdf' wirklich löschen?" (i18n/tr :delete-confirm "report.pdf")))
    (is (= "10 Zeilen x 5 Spalten" (i18n/tr :stats 10 5)))

    ;; Single-pass safety: "%s" or "$&" inside an argument must NOT be re-interpolated or mangled!
    (is (= "100%s Zeilen x $100 & $& \\path Spalten"
           (i18n/tr :stats "100%s" "$100 & $& \\path")))

    ;; Positional reordering (%1$s / %2$s and {0} / {1})
    (is (= "In #general auf Alice antworten"
           (i18n/tr :reordered "Alice" "#general")))
    (is (= "100 enthält 25"
           (i18n/tr :indexed 25 100)))

    (i18n/with-lang :en
      (is (= "Reply to Alice in #general"
             (i18n/tr :reordered "Alice" "#general")))
      (is (= "25 of 100"
             (i18n/tr :indexed 25 100))))

    ;; Named map interpolation {user}, {count} (including 0)
    (is (= "Hallo Alice, 0 Einträge"
           (i18n/tr :named {:user "Alice" :count 0})))

    ;; Direct i18n-map with format args
    (is (= "Hallo Alice!" (i18n/tr {:i18n true :de "Hallo %s!" :en "Hello %s!"} "Alice")))
    ;; Nil handling in tr
    (is (= "" (i18n/tr nil))))

  (testing "Scoped dictionary lookup via tr-in (interchangeable string & keyword keys)"
    (i18n/set-default-lang! :de)
    (let [field-labels {"createdAt" {:en "Created At" :de "Erstellt am"} ;; no :i18n true needed
                        :items {:en "Items (%s)" :de "Einträge (%s)"}}]
      ;; String key looked up by string or keyword
      (is (= "Erstellt am" (i18n/tr-in field-labels "createdAt")))
      (is (= "Erstellt am" (i18n/tr-in field-labels :createdAt)))
      ;; Keyword key looked up by keyword or string with format arg
      (is (= "Einträge (42)" (i18n/tr-in field-labels :items 42)))
      (is (= "Einträge (42)" (i18n/tr-in field-labels "items" 42)))
      (i18n/with-lang :en
        (is (= "Created At" (i18n/tr-in field-labels :createdAt)))
        (is (= "Items (42)" (i18n/tr-in field-labels "items" 42)))))))

(deftest macros-test
  (testing "with-lang"
    (i18n/set-default-lang! :en)
    (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))

    (i18n/with-lang :de
      (is (= "Hallo" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))

      ;; Nested
      (i18n/with-lang :en
        (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))))))
