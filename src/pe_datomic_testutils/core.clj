(ns pe-datomic-testutils.core
  (:require [pe-datomic-utils.core :as ducore]))

(defn make-db-refresher-fixture-fn
  "Returns a function suitable for use as a fixture function for a set of unit
  tests.  The returned function deletes the Datomic database at db-uri, creates
  it, transacts the set of given schema files and transacts the given partition
  (creates it).  Like any test fixture function, the returned function receives
  as its input a function, and invokes it."
  [db-uri conn partition schema-files]
  (fn [f]
    (reset! conn (ducore/refresh-db db-uri schema-files))
    (ducore/transact-partition @conn partition)
    (f)))
