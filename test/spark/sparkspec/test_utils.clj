(ns spark.sparkspec.test-utils
  (:require [datomic.api :as db]
            [spark.sparkspec.datomic :refer [spark-type-attr]]))

;;;; Macro for using fresh datomic instances for every test.

(def ^:dynamic *conn*)
(defn db [] (db/db *conn*))
(defn uri [] (str "datomic:mem://" (gensym "temporary-database")))

(defn make-db [schema] 
  (let [uri (uri), db (db/create-database uri), c (db/connect uri)]
    @(db/transact c schema)
    c))

(defmacro with-test-db [schema & body]
  `(binding [*conn* (make-db ~schema)]
     ~@body))
