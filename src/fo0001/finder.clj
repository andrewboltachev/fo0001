(ns fo0001.finder
  (:require
   [rewrite-clj.zip :as z]
   [rewrite-clj.zip.walk :as zw]
   [rewrite-clj.node :as n]
   [rewrite-clj.node.protocols :as np]
   [rewrite-clj.custom-zipper.core :as cz]))

(def ^:dynamic marker-start "\u001b[34m")
(def ^:dynamic marker-end "\u001b[m")

(defrecord MarkerNode [child]
  np/Node
  (sexpr [_] (np/sexpr child))
  (printable-only? [_] false)
  (string [this]
    (str
     marker-start
     (np/string child)
     marker-end)))

(defn find-and-mark [data p?]
  edited (zw/postwalk
          data
          #(-> % z/sexpr p?)
          #(cz/replace % (MarkerNode. (z/node %)))))

(defn print-out [data]
  (z/print-root data))
