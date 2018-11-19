(ns fo0001.handler
  (:require
    
    [ns-tracker.core :refer [ns-tracker]]
    ))

(defonce mn (ns-tracker ["src"]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body
   (str
   (clojure.string/join "\n"
    (map pr-str (mn))
    )
     "\n"
     )
   #_"Hello World!\n"})
