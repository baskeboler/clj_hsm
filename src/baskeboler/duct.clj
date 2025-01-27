(ns baskeboler.duct
  (:require [integrant.core :as ig]
            [baskeboler.hsm.core :as hsm]
            [duct.logger :as log]))

(defmethod ig/init-key ::hsm [_ {:keys [logger] :as _opts}]
  (log/info logger :duct/creating-hms {})
  (hsm/create-hms) )
