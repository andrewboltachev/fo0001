(ns fo0001.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-body]]
            [fo0001.base :refer [do-find-operation]]))

(defn handler [request]
  (let [[path needle] (get-in request [:body :args])]

    (do-find-operation path needle {:print-start-end? true})

    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body
     "Hello World! yeah!\n"}))

(defn run-server []
  (run-jetty
     (->
      #'handler
      (wrap-json-body {:keywords? true})
      wrap-reload)
     {:port 57821}))
