(ns baskeboler.handler
  (:require [integrant.core :as ig]
            [baskeboler.hsm.protocol :as hsm]
            [duct.logger :as log]
            [duct.module.web]
            [duct.middleware.web]
            [ring.util.response :as res]))

(defmethod ig/init-key ::encrypt [_ {:keys [logger hsm]}]
  (fn [req]
    ;; (lert )
    ;; (log/info logger :hsm/show-info (str (:body req)))
    (println "encrypt")
    {:status 200 :body ""}))