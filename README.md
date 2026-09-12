# clj-helper

`clj-helper` is a Clojure and ClojureScript library providing a collection of utility functions for common tasks like string manipulation, vector operations, map updates, compression, and cryptographic helpers.

Designed for cross-platform compatibility, most modules are written in `.cljc`, ensuring they work seamlessly in both JVM and Browser / Node.js environments.

## Features

- **`clj-helper.string`**: Random code generation (pseudo-random and cryptographically secure), unique IDs, string shortening, and parsing.
- **`clj-helper.vector`**: Advanced vector operations like moving elements, removing by index, and safe lookups by key/value.
- **`clj-helper.map`**: Utilities for updating map values and checking key equality.
- **`clj-helper.binary`**: Multi-byte UTF-8 encoding/decoding, Base64 conversions, and byte buffer helpers (cross-platform).
- **`clj-helper.compression`**: String compression and decompression with decompression bomb protection (JVM only, using XZ).
- **`clj-helper.crypto`**: SHA-256, SHA-1, and MD5 hashing (cross-platform, zero extra dependencies).
- **`clj-helper.edn`**: EDN serialization and deserialization helpers.
- **`clj-helper.filter`**: Flexible filtering functions (searchfilter, keyfilter, multifilter).
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

### String Helpers

```clojure
(require '[clj-helper.string :as s])

(s/get-random-code 8) 
;; => "7A9B2C4D" (pseudo-random)

(s/get-secure-random-code 16)
;; => "TNATKF9O3SWI9XYZ" (cryptographically secure)

(s/get-unique-id "USR")
;; => "20260905T100000-USR123"

(s/shorten "This is a long string" 10)
;; => "This is a "
```

### Cryptographic Helpers (Cross-Platform)

```clojure
(require '[clj-helper.crypto :as crypto])

(crypto/string->sha256 "secret")
;; => "2bb80e3bad52f13c5b40ecced17b995d813b1fb35ce1f3e583ff500a249d1022"

(crypto/string->sha1 "checksum")
;; => "8f310f8a9645f771da4cb4ae2adff91f4b8253fe"

(crypto/string->md5 "checksum")
;; => "341be97d9aff90c9978347f66f945b77"
```

### Vector Helpers

```clojure
(require '[clj-helper.vector :as v])

(def my-vec ["a" "b" "c" "d"])

(v/move-right my-vec 1)
;; => ["a" "c" "b" "d"]

(v/remove-nth my-vec 2)
;; => ["a" "b" "d"]

(v/get-index-by [{:id 1 :name "A"} {:id 2 :name "B"}] :id 2)
;; => 1

(v/remove-by [{:id 1 :name "A"} {:id 2 :name "B"}] :id 2)
;; => [{:id 1 :name "A"}]
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
    c/string->xz->base64
    c/base64->xz->string)
;; => "Some text to compress"
```

## Development

### Running Tests

To run the tests on the JVM, use Leiningen:

```bash
lein test
```

### Generating Documentation

API documentation is generated with [Codox](https://github.com/weavejester/codox) and published to GitLab Pages:

```bash
lein codox
```

The generated HTML documentation will be placed in `target/doc/`.

## Documentation

The API reference is published automatically via GitLab Pages:
- [API Documentation](https://ww-software.fh-muenster.io/libraries/clj-helper)

## Usage Notice

This software is result of an experimental research project developed as part of work at FH Münster. Its release as open source software is intended to facilitate collaboration and further development within the community.

The software is provided in its current state "as is". Any deployment, hosting, or production use is undertaken entirely at your own risk. Neither the developers nor FH Münster assume any warranty or liability for the stability, security, correctness, or fitness of the software for any particular purpose.

For detailed legal terms and disclaimers, please refer to the accompanying Eclipse Public License 2.0 (EPL-2.0).

## Copyright and License

Copyright © 2026 Bruno Burke  
Copyright © 2020-2026 FH Münster and contributors  

Distributed under the Eclipse Public License version 2.0.


