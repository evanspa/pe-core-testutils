(ns pe-core-testutils.core
  (:require [datomic.api :as d]
            [pe-datomic-utils.core :as ducore]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Test fixture maker
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn make-db-refresher-fixture-fn
  [db-uri conn partition schema-files]
  (fn [f]
    (reset! conn (ducore/refresh-db db-uri schema-files))
    (ducore/transact-partition @conn partition)
    (f)))
