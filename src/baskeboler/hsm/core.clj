(ns baskeboler.hsm.core
  (:require [baskeboler.hsm.protocol :as protocol]
            [baskeboler.hsm.impl :as impl]))



(defn ^:export create-hms []
  (let [hms (impl/->Hms nil {})]
    (protocol/init hms)))