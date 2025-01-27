(ns baskeboler.hsm.protocol)

(defprotocol HmsProtocol
  (init  [this]
    [this mkey])
  (import-key [this key-bytes alias])
  (generate-key [this] ) 
  (encrypt [this kbytes data])
  (decrypt [this kbytes data]))