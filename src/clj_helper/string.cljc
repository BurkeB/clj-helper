(ns clj-helper.string
  (:require [clojure.string :as string]
            #?(:clj [clojure.edn :as edn]
               :cljs [cljs.reader :as edn]))
  #?(:clj (:import [java.time Instant])))

(defn str= [a1 a2]
  (= (str a1) (str a2)))

(defn set->str [set]
  (pr-str set))

(defn str->set [str]
  (edn/read-string str))

(defn shorten [string n]
  (when string
    (subs string 0 (min n (count string)))))

(defn safe-name [val]
  (if (keyword? val)
    (name val)
    (if (string? val)
      val
      "")))

(defn get-random-code [length]
  (let [chars (string/split "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" #"")]
    (apply str (take length (repeatedly #(rand-nth chars))))))

(defn get-unique-id [prefix]
  (let [now #?(:cljs (.toISOString (new js/Date))
               :clj (.toString (Instant/now)))
        code (str (gensym (shorten prefix 4)))]
    (shorten (str
              #?(:cljs (.join (.split (.substr now 0 19) ":") "-")
                 :clj (-> now
                          (shorten 19)
                          (string/replace #"(-|\:)" "")
                          )) "-" code)
             32)))

(defn date->str [date]
  (try
    (.toLocaleString date)
    (catch
        #?(:cljs js/Error
           :clj Exception) e
      (str date))))


(defn parse-int [s]
  #?(:clj (try
            (Integer/parseInt s)
            (catch Exception e
              nil))
     :cljs (try
             (js/parseInt s)
             (catch :default e
               nil))))
