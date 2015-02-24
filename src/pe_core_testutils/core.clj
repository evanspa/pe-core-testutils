(ns pe-core-testutils.core
  (:require [datomic.api :as d]
            [pe-datomic-utils.core :as ducore]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Test fixture maker
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn make-db-refresher-fixture-fn
  [db-uri conn partition & schema-filenames]
  (fn [f]
    (reset! conn (ducore/refresh-db db-uri schema-filenames))
    @(d/transact @conn [{:db/id (d/tempid :db.part/db)
                         :db/ident partition
                         :db.install/_partition :db.part/db}])
    (f)))
