(ns clj-helper.map)

(defn update-values [umap ufn]
  (into {}
        (map
         (juxt first (comp ufn last))
         umap)))
