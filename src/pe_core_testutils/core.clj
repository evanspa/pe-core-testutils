(ns pe-core-testutils.core
  (:require [datomic.api :as d]
            [clojure.pprint :refer (pprint)]
            [clojure.java.io :refer [resource]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Database refresh functionality
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn refresh-db [db-uri schema-files]
  (do
    (d/delete-database db-uri)
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      (doseq [schema-file schema-files]
        @(d/transact conn (read-string (slurp (resource schema-file)))))
      conn)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Test fixture maker
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn make-db-refresher
  [db-uri conn partition & schema-filenames]
  (fn [f]
    (reset! conn (refresh-db db-uri schema-filenames))
    @(d/transact @conn [{:db/id (d/tempid :db.part/db)
                         :db/ident partition
                         :db.install/_partition :db.part/db}])
    (f)))
