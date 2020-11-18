(ns clj-helper.crypto
  #?(:cljs (:require [goog.crypt]
                     [goog.crypt.Md5])))

#?(:cljs
   (defn string->md5 [str]
     (goog.crypt/byteArrayToHex
      (let [md5 (goog.crypt.Md5.)]
        (.update md5 (goog.crypt/stringToUtf8ByteArray str))
        (.digest md5)))))

