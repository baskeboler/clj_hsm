(ns baskeboler.hsm.protocol)

(defprotocol HmsProtocol
  "Protocol for Hardware Security Module (HSM)
    This protocol defines the operations that can be performed on the HSM
    The HSM is a secure device that can generate, store and manage cryptographic keys
    The HSM can also perform cryptographic operations such as encryption and decryption"
  (init  [this]
    [this mkey] "Initialize the HSM with a master key")
  (import-key [this key-bytes alias] "Import a key into the HSM")
  (generate-key [this] "Generate a key in the HSM")
  (encrypt [this kbytes data] "Encrypt data using a key in the HSM")
  (decrypt [this kbytes data] "Decrypt data using a key in the HSM"))