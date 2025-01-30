(ns baskeboler.hsm.core
  (:require [baskeboler.hsm.protocol :as protocol]
            [baskeboler.hsm.impl :as impl]))

(refer 'baskeboler.hsm.protocol)
(defn ^:export create-hms 
  "Create an instance of Hms"  
  []
  (let [hms (impl/->Hms nil {})]
    (protocol/init hms)))