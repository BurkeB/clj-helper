(ns clj-helper.map-test
  (:require [clj-helper.map :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer :all :include-macros true])))


(deftest keyqual-test
  (testing "keyqual with non-sequence keys"
    (let [data {:id 5 :a "hallo" :b :test}]
      (is (true? (sut/keyqual data :id 5)))
      (is (true? (sut/keyqual data :a "hallo")))
      (is (true? (sut/keyqual data :b :test)))))
  (testing "keyqual with keys sequence"
    (let [data {:nesting1 {:id 5 :a "hallo" :b :test}
                :nesting2 {:nesting3 {:id 6
                                      :a "hallo2"
                                      :b :test2}}}]
      (is (true? (sut/keyqual data [:nesting1 :id] 5)))
      (is (true? (sut/keyqual data [:nesting1 :a] "hallo")))
      (is (true? (sut/keyqual data [:nesting1 :b] :test)))

      (is (true? (sut/keyqual data [:nesting2 :nesting3 :id] 6)))
      (is (true? (sut/keyqual data [:nesting2 :nesting3 :a] "hallo2")))
      (is (true? (sut/keyqual data [:nesting2 :nesting3 :b] :test2))))))
