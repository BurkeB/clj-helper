(ns clj-helper.binary
  #?(:clj (:import [java.time LocalDateTime]
                   [java.util Base64])))


#?(:clj
   (defn byte-buffer->byte-array [Buffer]
     (let [buffer-size (.capacity Buffer)
           array (byte-array buffer-size)]
       (.get Buffer array 0 buffer-size)
       array)))

#?(:clj
   (defn byte-array->base64 [byte-array]
     (let [encoder (Base64/getEncoder)]
       (.encodeToString encoder (.encode encoder byte-array))
     )))

