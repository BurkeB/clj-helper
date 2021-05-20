(ns clj-helper.set)

(defn toggle
  "Add or remove item 'value' from set 's'"
  [s value]
  (let [s (or s #{})
        update-fn (if (contains? s value) disj conj)]
    (update-fn s value)))
