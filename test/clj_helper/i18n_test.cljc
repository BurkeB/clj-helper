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
  (f))

(use-fixtures :each reset-config-fixture)

(deftest configuration-test
  (testing "Default language configuration"
    (i18n/set-default-lang! :de)
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

  (testing "Resolver registration"
    (i18n/set-default-lang! :en)
    (i18n/register-resolver! (constantly :es))
    (is (= "Hola" (i18n/i18n-get {:i18n true :en "Hello" :es "Hola"})))

    ;; Resolver precedence (first registered wins)
    (i18n/clear-resolvers!)
    (i18n/register-resolver! (constantly nil)) ;; Returns nil, should continue
    (i18n/register-resolver! (constantly :de))
    (is (= "Hallo" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo" :es "Hola"})))))

(deftest core-api-test
  (testing "i18n-map?"
    (is (true? (i18n/i18n-map? {:i18n true :en "Test"})))
    (is (false? (i18n/i18n-map? {:en "Test"})))
    (is (false? (i18n/i18n-map? "Not a map"))))

  (testing "available-langs"
    (is (= #{:en :de} (set (i18n/available-langs {:i18n true :en "A" :de "B"})))))

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

(deftest macros-test
  (testing "with-lang"
    (i18n/set-default-lang! :en)
    (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))

    (i18n/with-lang :de
      (is (= "Hallo" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))

      ;; Nested
      (i18n/with-lang :en
        (is (= "Hello" (i18n/i18n-get {:i18n true :en "Hello" :de "Hallo"})))))))
