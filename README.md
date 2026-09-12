# clj-helper

`clj-helper` is a Clojure and ClojureScript library providing a collection of utility functions for common tasks like internationalization, string manipulation, vector operations, map updates, filtering, compression, and cryptographic helpers.

Designed for cross-platform compatibility, most modules are written in `.cljc`, ensuring they work seamlessly in both JVM and Browser / Node.js environments with zero unnecessary dependencies.

## Features

- **`clj-helper.i18n`**: Simple, lightweight internationalization (i18n) with fallbacks, region support, resolvers, and dynamic scoping (`with-lang`).
- **`clj-helper.string`**: Random code generation (pseudo-random and cryptographically secure), high-precision unique IDs, string shortening, and parameter formatting.
- **`clj-helper.vector`**: Advanced vector operations like element swapping (`swap-at`), moving (`move`), insertion (`insert-at`), and safe lookups.
- **`clj-helper.filter`**: Multi-attribute filtering (`filter-by-attrs`) supporting wildcards, blacklists, and case-insensitive search.
- **`clj-helper.map`**: Utilities for updating map values and checking key equality (`key-equals?`).
- **`clj-helper.binary`**: Multi-byte UTF-8 encoding/decoding, Base64 conversions, and byte buffer helpers (cross-platform).
- **`clj-helper.crypto`**: SHA-256, SHA-1, and MD5 hashing, plus Base64 encoding (cross-platform, zero extra dependencies).
- **`clj-helper.compression`**: String compression and decompression with decompression bomb protection (JVM only, using XZ).
- **`clj-helper.edn`**: EDN serialization and deserialization helpers (`edn->str`, `str->edn`).
- **`clj-helper.set`**: Set toggling utilities.

## Installation

Add the following dependency to your `project.clj`:

```clojure
[de.bburke/clj-helper "0.1.0"]
```

Or to your `deps.edn`:

```clojure
de.bburke/clj-helper {:mvn/version "0.1.0"}
```

## Usage

### Internationalization (`clj-helper.i18n`)

Map-based i18n supporting fallbacks, active language resolvers, and dynamic scoping:

```clojure
(require '[clj-helper.i18n :as i18n])

;; Configure default language and fallback order
(i18n/set-default-lang! :en)
(i18n/set-fallback-order! [:en])

;; Example i18n map
(def greeting {:i18n true, :en "Hello", :de "Hallo", :fr "Bonjour"})

;; Resolves based on current active language
(i18n/i18n-get greeting) ; => "Hello"

;; Request a specific language directly
(i18n/i18n-get greeting :de) ; => "Hallo"

;; Regional fallback: :de-AT falls back to :de
(i18n/i18n-get {:i18n true, :de "Hallo"} :de-AT) ; => "Hallo"

;; Dynamic scope (ideal for server-side rendering or per-request context)
(i18n/with-lang :fr
  (i18n/i18n-get greeting)) ; => "Bonjour"
```

### String Helpers

```clojure
(require '[clj-helper.string :as s])

(s/rand-code 8) 
;; => "7A9B2C4D" (pseudo-random)

(s/get-secure-random-code 16)
;; => "TNATKF9O3SWI9XYZ" (cryptographically secure)

(s/unique-id "usr")
;; => "20260912102500123G1-usr"

(s/shorten "This is a long string" 10)
;; => "This is a "

(s/parse-int "42")
;; => 42
```

### Filtering (`clj-helper.filter`)

```clojure
(require '[clj-helper.filter :as f])

(def users [{:id 1 :role "admin" :status "active"}
            {:id 2 :role "user" :status "active"}
            {:id 3 :role "user" :status "inactive"}])

;; Filter by exact match
(f/filter-by-attrs users {:role "user" :status "active"})
;; => ({:id 2, :role "user", :status "active"})

;; Filter with wildcard
(f/filter-by-attrs users {:role "user" :status "*"})
;; => ({:id 2 ...} {:id 3 ...})
```

### Vector Helpers

```clojure
(require '[clj-helper.vector :as v])

(def my-vec ["a" "b" "c" "d"])

(v/swap-at my-vec 0 3)
;; => ["d" "b" "c" "a"]

(v/move-right my-vec 1)
;; => ["a" "c" "b" "d"]

(v/insert-at my-vec 2 "x")
;; => ["a" "b" "x" "c" "d"]

(v/remove-at my-vec 2)
;; => ["a" "b" "d"]

(v/get-by [{:id 1 :name "A"} {:id 2 :name "B"}] :id 2)
;; => {:id 2 :name "B"}
```

### Cryptographic Helpers (Cross-Platform)

```clojure
(require '[clj-helper.crypto :as crypto])

(crypto/sha256 "secret")
;; => "2bb80e3bad52f13c5b40ecced17b995d813b1fb35ce1f3e583ff500a249d1022"

(crypto/sha1 "checksum")
;; => "8f310f8a9645f771da4cb4ae2adff91f4b8253fe"

(crypto/md5 "checksum")
;; => "341be97d9aff90c9978347f66f945b77"

(crypto/base64-encode "Hello World")
;; => "SGVsbG8gV29ybGQ="
```

### EDN Utilities

```clojure
(require '[clj-helper.edn :as edn])

(edn/edn->str {:a 1 :b [2 3]})
;; => "{:a 1,\n :b [2 3]}\n"

(edn/str->edn "{:a 1 :b [2 3]}")
;; => {:a 1, :b [2 3]}
```

### Binary & Base64 (UTF-8, Cross-Platform)

```clojure
(require '[clj-helper.binary :as b])

(b/string->base64 "Hello World")
;; => "SGVsbG8gV29ybGQ="

(b/base64->string "SGVsbG8gV29ybGQ=")
;; => "Hello World"
```

### Compression (JVM only)

```clojure
(require '[clj-helper.compression :as c])

(-> "Some text to compress"
    c/compress->base64
    c/base64->decompress)
;; => "Some text to compress"
```

## Development

### Running Tests

To run the tests on the JVM, use Leiningen:

```bash
lein test
```

### Generating Documentation

API documentation is generated with [Codox](https://github.com/weavejester/codox):

```bash
lein codox
```

The generated HTML documentation will be placed in `target/doc/`.

## Usage Notice

Originally developed as part of an experimental research project at FH Münster (2020–2026), this library is now independently maintained and developed further by Bruno Burke.

The software is provided in its current state "as is". Any deployment, hosting, or production use is undertaken entirely at your own risk. Neither the developers nor FH Münster assume any warranty or liability for the stability, security, correctness, or fitness of the software for any particular purpose.

For detailed legal terms and disclaimers, please refer to the accompanying Eclipse Public License 2.0 (EPL-2.0).

## Copyright and License

Copyright © 2026 Bruno Burke  
Copyright © 2020-2026 FH Münster and contributors  

Distributed under the Eclipse Public License version 2.0.
