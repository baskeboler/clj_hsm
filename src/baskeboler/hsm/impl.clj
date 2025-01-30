(ns baskeboler.hsm.impl
  (:require [baskeboler.hsm.protocol :as protocol])
  (:import 
           [javax.crypto KeyGenerator Cipher SecretKeyFactory]
           [javax.crypto.spec DESedeKeySpec]))

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



(defn- bytes->key [kbytes]
  (let [spec      (DESedeKeySpec. kbytes)
        k-factory (SecretKeyFactory/getInstance "DESede")]
    (.generateSecret k-factory spec)))

(defn- generate-key* []
  (let [keygen (KeyGenerator/getInstance "DESede")
        secret-key (.generateKey keygen)]
    secret-key))


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
        (assoc :master-key (generate-key*))))
  (init [this mkey]
    (let [k (bytes->key mkey)]
      (-> this (assoc :master-key k))))

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
         new-key       (transform-key kbytes (:master-key this))
         ]
     (.init cipher Cipher/DECRYPT_MODE new-key)
     (.doFinal cipher data))))
