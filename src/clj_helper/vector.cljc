(ns clj-helper.vector)

(defn inside?
  "checks if pos is valid index in coll"
  [coll pos]
  (and (< pos (count coll)) (>= pos 0)))

(defn remove-nth
  "remove elem in coll"
  [coll pos]
  (if-not (inside? coll pos)
    coll
    (vec (concat (subvec coll 0 pos) (subvec coll (inc pos))))))

(defn insert
  "insert elem in coll at pos"
  [coll element pos]
  (if-not (<= 0 pos (count coll))
    coll
    (reduce conj (reduce conj (subvec coll 0 pos) [element]) (subvec coll pos))))

(defn move-left
  "move elem in coll to the left"
  [coll pos]
  (let [elem (get coll pos)]
    (if (or (zero? pos) (not (inside? coll pos)))
      coll
      (-> coll
          (remove-nth pos)
          (insert elem (dec pos))))))

(defn move-right
  "move elem in coll to the right"
  [coll pos]
  (let [elem (get coll pos)]
    (if (or (= pos (dec (count coll))) (not (inside? coll pos)))
      coll
      (-> coll
          (remove-nth pos)
          (insert elem (inc pos))))))

(defn move
  "move elem from 'from' to 'to'"
  [coll from to]
  (let [elem (get coll from)]
    (if (or (not (inside? coll to)) (not (inside? coll from)))
      coll
      (let [elem (nth coll from)
            to (if (> to from)
                 to
                 to)]
        (-> coll
            (remove-nth from)
            (insert elem to))))))

(defn get-from-array [array key value]
  (first (filter #(= (get % key) value) array)))

(defn get-by [coll key val]
  (some #(when (= (key %) val) %) coll))

(defn get-index-by [coll key val]
  (first (keep (fn [[index element]]
                 (when (= (key element) val)
                   index))
               (map-indexed vector coll))))

(defn remove-by [coll key val]
  (let [index (get-index-by coll key val)]
    (remove-nth coll index)))

(defn mapvec-to-map [vec]
  (into {} (map (juxt :id identity)) vec))


(defn vconj [coll element]
  (if (vector? coll)
    (conj coll element)
    (recur (vec coll) element)))
