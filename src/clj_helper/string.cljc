(ns clj-helper.string
  (:require [clojure.string :as string]
            #?(:clj [clojure.edn :as edn]
               :cljs [cljs.reader :as edn]))
  #?(:clj (:import [java.time LocalDateTime])))



(defn str= [a1 a2]
  (= (str a1) (str a2)))

(defn set->str [set]
  (pr-str set))

(defn str->set [str]
  (edn/read-string str))

(defn shorten [string n]
  (subs string 0 (min n (count string))))

(defn get-random-code [length]
  (let [chars (string/split "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" #"")]
    (apply str (take length (repeatedly #(rand-nth chars))))))

(defn get-unique-id [prefix]
  (let [now #?(:cljs (.toISOString (new js/Date))
               :clj (.toString (LocalDateTime/now)))]
    (shorten (str
              #?(:cljs (.join (.split (.substr now 0 19) ":") "-")
                 :clj (-> now
                          (shorten 19)
                          (string/replace #"-" "")
                          (string/split #"\:")
                          (->> (string/join ""))
                          )) "-" (gensym (shorten prefix 4)))
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
