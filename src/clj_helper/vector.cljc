(ns clj-helper.vector)

(defn inside? [coll pos]
  "checks if pos is valid index in coll"
  (and (< pos (count coll)) (>= pos 0)))

(defn remove
  "remove elem in coll"
  [coll pos]
  (if-not (inside? coll pos)
    coll
    (vec (concat (subvec coll 0 pos) (subvec coll (inc pos))))))

(defn insert
  "insert elem in coll"
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
         (remove pos)
         (insert elem (dec pos))))))

(defn move-right
  "move elem in coll to the right"
  [coll pos]
  (let [elem (get coll pos)]
    (if (or (= pos (dec (count coll))) (not (inside? coll pos)))
      coll
     (-> coll
         (remove pos)
         (insert elem (inc pos))))))

(defn get-from-array [array key value]
  (first (filter #(= (get % key) value) array)))
