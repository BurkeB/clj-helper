(ns clj-helper.filter
  (:require [clojure.string :refer [includes? lower-case]]))

(defn searchfilter [string substring]
  (when (string? string)
    (includes? (lower-case string) (lower-case substring))))

(defn keyfilter [map substring keys]
  (some #(searchfilter (get map %) substring) keys))

(defn multifilter [map attributes]
  (every?
   (fn [[key value]]
     (if (or (nil? value) (= "*" value) (= :any value))
       map
       (case key
         :all (keyfilter map value (keys map))
         :blacklist (not ((set (:blacklist attributes)) (:id map)))
         (= (get map key) value))))
   attributes))
