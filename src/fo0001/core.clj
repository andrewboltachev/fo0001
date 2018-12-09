(ns fo0001.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-body]]
            [clojure.tools.reader :as r]
            [rewrite-clj.reader :refer [file-reader]]
            [rewrite-clj.parser]
            [cljs.tagged-literals]
            [puget.printer :refer [cprint]]
            [fo0001.finder :refer [find-and-print
                                   marker-start]]))

(defn tree-seq1 [root]
  (tree-seq #(or (sequential? %) (map? %)) #(if (map? %) (concat (keys %) (vals %)) %) root))

(defn handler [request]
  (let [[path needle] (get-in request [:body :args])

        is-a-function? (clojure.string/starts-with? needle "!")
        needle (cond->
                needle
                 is-a-function? (subs 1))
        needle (r/read-string needle)
        needle (cond->
                needle
                 is-a-function? eval)

        file-list (file-seq (clojure.java.io/file path))

        clojure-file-list (filter
                           (fn [file]
                             (clojure.string/ends-with? (.getName file) ".cljs"))
                           file-list)

        read1 (fn [ipbr]
                (try
                  (rewrite-clj.parser/parse ipbr)
                  (catch Exception e)))]
    (binding [clojure.tools.reader/*data-readers* cljs.tagged-literals/*cljs-data-readers*]
      (println "~~~ NEW SEARCH ~~~" needle is-a-function?)
      (doseq [file clojure-file-list
              :let [ipbr (file-reader file)]
              ;:when (= (.getPath file) "/home/andrey/pribrano/pribrano/CreatedByMe/Programming/Work/Upwork/Sebastien_Beal/admin-dashboard/src/cljs/locarise/subs.cljs")
              ]
        (loop [v (read1 ipbr)]
          (when v
            (let [p? (if is-a-function?
                       needle
                       #(= needle %))
                  search-result (with-out-str (find-and-print v p?))]
              (when (clojure.string/includes? search-result marker-start)
                (println "\u001b[34m" (.getPath file) "\u001b[m")
                (println search-result))
              (recur
               (read1 ipbr))))))
      (println "~~~ END ~~~"))

    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body
     "Hello World! yeah!\n"}))

(defn -main []
  "I don't do a whole lot."
  (run-jetty
   (->
    #'handler
    (wrap-json-body {:keywords? true})
    wrap-reload)
   {:port 57821}))
