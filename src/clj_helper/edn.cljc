(ns clj-helper.edn
  (:require [clojure.string :as string]
            #?(:clj [clojure.edn :as edn]
               :cljs [cljs.reader :as edn])
            #?(:clj [clojure.pprint :as pprint]
               :cljs [cljs.pprint :as pprint])))


(defn serialize-edn [edn-data]
  (with-out-str (pprint/pprint edn-data)))

(defn deserialize-edn [ednstr]
  (edn/read-string ednstr))

#?(:cljs
   (defn clj->json [object]
     (js/JSON.stringify (clj->js object) nil 2)))
