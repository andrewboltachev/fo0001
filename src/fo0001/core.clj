(ns fo0001.core
  (:require [fo0001.base :refer [do-find-operation]]))

(defn -main [arg1 & args]
  "I don't do a whole lot."
  (do-find-operation "." arg1 {}))
