(ns fo0001.finder
  (:require
   [rewrite-clj.zip :as z]
   [rewrite-clj.zip.walk :as zw]
   [rewrite-clj.node :as n]
   [rewrite-clj.node.protocols :as np]
   [rewrite-clj.custom-zipper.core :as cz]))

(def ^:dynamic marker-start "\u001b[31m\u001b[1m")
(def ^:dynamic marker-end "\u001b[m")

(defrecord MarkerNode [child]
  np/Node
  (sexpr [_]
    (when-not
      (np/printable-only? child)
      (np/sexpr child)))
  (printable-only? [_]
    (np/printable-only? child))
  (string [this]
    (str
     marker-start
    ;(if-not
    ;  (np/printable-only? child)
      (np/string child)
    ;  (pr-str child))
     marker-end)))

(defn find-and-mark [data p?]
  (zw/prewalk
    data
    #(and
       (not (np/printable-only? (z/node %)))
       (-> % z/sexpr p?))
    #(cz/replace % (MarkerNode. (z/node %)))))

(defn print-out [data]
  (z/print-root data))

(defn find-and-print [data p?]
  (->
    data
    z/edn
    (find-and-mark p?)
    print-out))
