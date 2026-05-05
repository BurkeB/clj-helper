# clj-helper

`clj-helper` is a Clojure and ClojureScript library providing a collection of utility functions for common tasks like string manipulation, vector operations, map updates, compression, and cryptographic helpers.

Designed for cross-platform compatibility, most modules are written in `.cljc`, ensuring they work seamlessly in both JVM and Browser environments.

## Features

- **`clj-helper.string`**: Random code generation, unique IDs, string shortening, and basic parsing.
- **`clj-helper.vector`**: Advanced vector operations like moving elements, removing by index, and finding elements by key/value.
- **`clj-helper.map`**: Utilities for updating map values and checking key equality.
- **`clj-helper.binary`**: Base64 encoding/decoding and byte array conversions.
- **`clj-helper.compression`**: String compression and decompression (JVM only, using XZ).
- **`clj-helper.crypto`**: Hashing and cryptographic utilities.
- **`clj-helper.edn`**: EDN reading and writing helpers.
- **`clj-helper.filter`**: Specialized filtering functions.

## Installation

Add the following dependency to your `project.clj`:

```clojure
[wwsoftware/clj-helper "0.0.3.13"]
```

Or to your `deps.edn`:

```clojure
wwsoftware/clj-helper {:mvn/version "0.0.3.13"}
```

## Usage

### String Helpers

```clojure
(require '[clj-helper.string :as s])

(s/get-random-code 8) 
;; => "7A9B2C4D"

(s/get-unique-id "USR")
;; => "20230505-1234-USR123"

(s/shorten "This is a long string" 10)
;; => "This is a "
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
```

### Compression (JVM only)

```clojure
(require '[clj-helper.compression :as c])

(-> "Some text"
    c/string->xz->base64
    c/base64->xz->string)
;; => "Some text"
```

## Development

### Running Tests

To run the tests, use Leiningen:

```bash
lein test
```

## Copyright and License

Copyright © 2020-2026 FH Münster, Bruno Burke.

Distributed under the Eclipse Public License version 2.0.
