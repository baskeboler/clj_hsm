(ns baskeboler.hsm.impl
  (:require
   [baskeboler.hsm.protocol :as protocol])
  (:import
   [java.time Instant]
   [javax.crypto Cipher KeyGenerator SecretKeyFactory]
   [javax.crypto.spec DESedeKeySpec SecretKeySpec]))

(comment
  (def keygen (KeyGenerator/getInstance "DESede"))
  (def secret-key (.. keygen (generateKey)))
  (def key-bytes (.getEncoded secret-key))
  (def key-spec (DESedeKeySpec. key-bytes))
  (def key-factory (SecretKeyFactory/getInstance "DESede"))
  (.. key-factory (generateSecret key-spec) (getEncoded))
  (def cipher (Cipher/getInstance "DESede/ECB/PKCS5Padding"))
  (. cipher
     (init Cipher/ENCRYPT_MODE secret-key))
  (.getBytes "hey there dude")
  (. cipher
     (doFinal (.getBytes "hey there dude"))))


(comment
  (def keygen (KeyGenerator/getInstance "AES"))
  (def secret-key (.. keygen (generateKey)))
  (def key-bytes (.getEncoded secret-key))
  (def key-spec (SecretKeySpec. key-bytes "AES"))
  (def key-factory (SecretKeyFactory/getInstance "AES/ECB/PKCS5Padding"))
  (.. key-factory (generateSecret key-spec) (getEncoded)))


(defn- bytes->key [kbytes]
  (let [spec      (DESedeKeySpec. kbytes)
        k-factory (SecretKeyFactory/getInstance "DESede")]
    (.generateSecret k-factory spec)))

(defn- generate-key* []
  (let [keygen (KeyGenerator/getInstance "DESede")
        secret-key (.generateKey keygen)]
    secret-key))

(defn- bytes->hex
  "Converts a byte array to a hex string"
  [bytes]
  (apply str (map #(format "%02x" %) bytes)))

(defn- hex->bytes 
  "Converts a hex string to a byte array"
  [hex]
  (let [bytes (byte-array (/ (count hex) 2))]
    (dotimes [i (/ (count hex) 2)]
      (aset bytes i (Integer/parseInt (subs hex (* i 2) (+ (* i 2) 2)) 16)))
    bytes))

(defn- transform-key [kbytes mkey]
  (let [cipher        (Cipher/getInstance "DESede/ECB/PKCS5Padding")
        _             (. cipher
                         (init Cipher/ENCRYPT_MODE mkey))
        new-key-bytes (. cipher (doFinal kbytes))]
    (bytes->key new-key-bytes)))

(defrecord Hms [master-key registered-keys]
  protocol/HmsProtocol
  (init
    [this]
    (-> this
        (assoc :master-key (generate-key*)
               :created (Instant/now))))
  (init [this mkey]
    (let [k (bytes->key mkey)]
      (-> this (assoc :master-key k
                      :created (Instant/now)))))

  (protocol/import-key
    [this kbytes alias]
    (let [key-spec (DESedeKeySpec. kbytes)
          factory  (SecretKeyFactory/getInstance "DESede")
          k        (.generateSecret factory key-spec)]
      (-> this
          (assoc-in [:registered-keys alias] k))))

  (protocol/generate-key
    [_this]
    (let [k (generate-key*)]
      (.getEncoded k)))

  (protocol/encrypt
    [this kbytes data]
    (let [cipher        (Cipher/getInstance "DESede/ECB/PKCS5Padding")
          new-key       (transform-key kbytes (:master-key this))]
      (.init cipher Cipher/ENCRYPT_MODE new-key)
      (.doFinal cipher data)))
  (protocol/decrypt
    [this kbytes data]
    (let [cipher        (Cipher/getInstance "DESede/ECB/PKCS5Padding")
          new-key       (transform-key kbytes (:master-key this))]
      (.init cipher Cipher/DECRYPT_MODE new-key)
      (.doFinal cipher data))))
