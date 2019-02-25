(ns fo0001.base
  (:require [clojure.tools.reader :as r]
            [rewrite-clj.reader :refer [file-reader]]
            [rewrite-clj.parser]
            [cljs.tagged-literals]
            [fo0001.finder :refer [find-and-print marker-start marker-end]]
            [puget.printer :refer [cprint]]))


(defn tree-seq1 [root]
  (tree-seq #(or (sequential? %) (map? %)) #(if (map? %) (concat (keys %) (vals %)) %) root))

(defn do-find-operation [path needle {:keys [a b print-start-end?] :or {a 0 b 0}}]
  (let [single-line-mode? (and (zero? a) (zero? b))
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
                             (or
                              (clojure.string/ends-with? (.getName file) ".clj")
                              (clojure.string/ends-with? (.getName file) ".cljs")))
                           file-list)

        read1 (fn [ipbr]
                (try
                  (rewrite-clj.parser/parse ipbr)
                  (catch Exception e)))]
    (binding [clojure.tools.reader/*data-readers* cljs.tagged-literals/*cljs-data-readers*]
      (when print-start-end?
        (println "~~~ NEW SEARCH ~~~" needle is-a-function?))
      (doseq [file clojure-file-list
              :let [ipbr (file-reader file)]]
        (loop [v (read1 ipbr)]
          (when v
            (let [p? (if is-a-function?
                       needle
                       #(= needle %))
                  search-result (with-out-str (find-and-print v p?))]
              (when (clojure.string/includes? search-result marker-start)
                (when-not single-line-mode?
                  (println "\u001b[34m" (.getPath file) "\u001b[m"))
                (let [lines (clojure.string/split-lines search-result)
                      indices (loop [result []
                                     [h & t] (map-indexed list lines)
                                     flag false]
                                (if
                                 (nil? h)
                                  result
                                  (let [[index line] h
                                        start? (clojure.string/includes? line marker-start)
                                        flag (or flag start?)

                                        incl? flag

                                        end? (clojure.string/includes? line marker-end)
                                        flag (and flag (not end?))]
                                    (recur
                                     (cond->
                                      result
                                       incl? (conj result index))
                                     t
                                     flag))))]

                  (doseq [[index line] (map-indexed list lines)
                          :when (contains? (set indices) index)
                          :let [begin (when-not (clojure.string/includes? line marker-start) marker-start)
                                end (when-not (clojure.string/includes? line marker-end) marker-end)]]
                    (when single-line-mode?
                      (print "\u001b[34m" (.getPath file) "\u001b[m"))
                    (println (str begin line end)))))

              (recur
               (read1 ipbr))))))
      (when print-start-end? (println "~~~ END ~~~")))))
