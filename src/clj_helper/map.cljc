(ns clj-helper.map)

(defn update-values [umap ufn]
  (into {}
        (map
         (juxt first (comp ufn last))
         umap)))

(defn keyqual
  "Checks if a key in a map is equal to a given value.
   If key is a vector the value is retrieved by 'get-in', otherwise by 'get'."
  [map key val]
  (if (sequential? key)
    (= (get-in map key) val)
    (= (get map key) val)))

