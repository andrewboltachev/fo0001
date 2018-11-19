(ns fo0001.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            ))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body
   "Hello World! yeah!\n"})

(defn- main []
  "I don't do a whole lot."
  (run-jetty
    (->
      #'handler
      wrap-reload)
    {:port 57821}))
