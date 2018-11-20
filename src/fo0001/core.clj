(ns fo0001.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-body]]
            [clojure.tools.reader :as r]
            [clojure.tools.reader.reader-types :refer [indexing-push-back-reader]]
            [cljs.tagged-literals]
            [puget.printer :refer [cprint]]))

(defn tree-seq1 [root]
  (tree-seq #(or (sequential? %) (map? %)) #(if (map? %) (concat (keys %) (vals %)) %) root))

(defn handler [request]
  (let [[path needle] (get-in request [:body :args])

        needle (r/read-string needle)

        file-list (file-seq (clojure.java.io/file path))

        clojure-file-list (filter
                           (fn [file]
                             (clojure.string/ends-with? (.getName file) ".cljs"))
                           file-list)

        read1 (fn [ipbr]
                (try
                  (clojure.tools.reader/read ipbr)
                  (catch Exception e)))]
    (binding [clojure.tools.reader/*data-readers* cljs.tagged-literals/*cljs-data-readers*]
      (doseq [file clojure-file-list
              :let [ipbr
                    (-> file
                        (java.io.FileReader.)
                        (java.io.PushbackReader.)
                        indexing-push-back-reader)]]
        (loop [v (read1 ipbr)]

          #_(prn

             (tree-seq1 v))

          (when (contains? (set (tree-seq1 v)) needle)
            (prn (.getPath file))
            (prn v)
            (newline))
          (when v
            (recur
             (read1 ipbr))))))

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
