;; Copyright © 2026 Bruno Burke
;; Copyright © 2020-2026 FH Münster and contributors
;;
;; This program and the accompanying materials are made available under the
;; terms of the Eclipse Public License 2.0 which is available at
;; https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns clj-helper.vector)

;; Index utilities
(defn max-index
  "Returns the highest valid index of a vector."
  [v]
  (dec (count v)))

(defn valid-index?
  "Checks if idx is within the bounds of vector v (inclusive)."
  [v idx]
  (and (integer? idx)
       (<= 0 idx (max-index v))))

(def inside? valid-index?)

;; Element removal and insertion
(defn remove-at
  "Removes the element at idx from vector v, returning a new vector."
  [v idx]
  (if (valid-index? v idx)
    (into (subvec v 0 idx) (subvec v (inc idx)))
    v))

(def remove-nth
  "Alias for remove-at for backward compatibility."
  remove-at)

(defn insert-at
  "Throws if idx is not an integer or out of bounds.
   Inserts x into vector v at position idx."
  [v idx x]
  {:pre [(integer? idx)
         (<= 0 idx (count v))]}
  (into (subvec v 0 idx)
        (cons x (subvec v idx))))

(defn insert-at-safe
  "Like insert-at, but returns original v on any error."
  [v idx x]
  (if (valid-index? v idx)
    (insert-at v idx x)
    v))

(defn insert
  "Insert element into coll at pos. If pos is out of bounds, returns coll."
  [coll element pos]
  (if (or (not (integer? pos)) (not (<= 0 pos (count coll))))
    coll
    (insert-at (vec coll) pos element)))

(defn is-first-index? [coll pos]
  (= pos 0))

(defn is-last-index? [coll pos]
  (= pos (max-index coll)))

;; Swap and move operations
(defn swap-at
  "Swaps elements at indices i and j in vector v."
  [v i j]
  (if (and (valid-index? v i) (valid-index? v j) (not= i j))
    (assoc v i (v j) j (v i))
    v))

(defn can-move-left?
  "Returns true if the element at idx can move left within vector v."
  [v idx]
  (and (valid-index? v idx)
       (> idx 0)))

(defn can-move-right?
  "Returns true if the element at idx can move right within vector v."
  [v idx]
  (and (valid-index? v idx)
       (< idx (max-index v))))

(defn move
  "Moves element from index 'from' to index 'to' within vector v.
   Optional opts:
   - :append-ok? (default false): whether to allow `to` to be (count v), appending the element."
  [v from to & {:keys [append-ok?] :or {append-ok? false}}]
  (let [max-to (if append-ok? (count v) (max-index v))]
    (if (and (valid-index? v from)
             (integer? to)
             (<= 0 to max-to))
      (let [x   (v from)
            v'  (remove-at v from)
            to' (cond
                  (< from to) (min (count v') to)
                  :else       to)]
        (insert-at v' to' x))
      v)))

(defn move-left
  "Swaps element at idx with its left neighbour."
  [v idx]
  (if (can-move-left? v idx)
    (swap-at v idx (dec idx))
    v))

(defn move-right
  "Swaps element at idx with its right neighbour."
  [v idx]
  (if (can-move-right? v idx)
    (swap-at v idx (inc idx))
    v))

(defn get-next-index-cycled
  "Returns (idx + 1) mod (count v), with invalid idx cycling to 0."
  [v idx]
  (if (or (not (valid-index? v idx))
          (= idx (max-index v)))
    0
    (inc idx)))

(defn get-prev-index-cycled
  "Returns (idx - 1) mod (count v), with invalid idx cycling to last index."
  [v idx]
  (if (or (not (valid-index? v idx))
          (zero? idx))
    (max-index v)
    (dec idx)))

(defn move-left-cycled [v idx]
  (if (valid-index? v idx)
    (move v idx (get-prev-index-cycled v idx))
    v))

(defn move-right-cycled [v idx]
  (if (valid-index? v idx)
    (move v idx (get-next-index-cycled v idx))
    v))

;; Finder functions
(defn get-from-array
  "Finds first map in a sequence where key k equals val."
  [arr k val]
  (some #(when (= (get % k) val) %) arr))

(defn- extract-val [element key]
  (cond
    (fn? key)         (key element)
    (sequential? key) (get-in element key)
    :else             (get element key)))

(defn get-by
  "Finds first element in coll where extracting with key matches val.
   key can be:
     • a function:    applied to each element
     • a vector path: get-in
     • any other key: get"
  [coll key val]
  (some
   #(when (= (extract-val % key) val) %)
   coll))

(defn get-index-by
  "Returns index of first element in coll where key/path matches val."
  [coll key val]
  (first
   (keep-indexed
    (fn [i e]
      (when (= (extract-val e key) val)
        i))
    coll)))

(defn remove-by
  "Removes first element in coll where key/path matches val."
  [coll key val]
  (if-let [index (get-index-by coll key val)]
    (remove-at coll index)
    coll))

;; Conversions
(defn mapvec-to-map
  "Converts a vector of maps with :id into a map keyed by :id."
  [vec]
  (into {} (map (juxt :id identity)) vec))

(defn vconj
  "Conj x onto coll, ensuring the result is a vector."
  [coll x]
  (conj (if (vector? coll) coll (vec coll)) x))
