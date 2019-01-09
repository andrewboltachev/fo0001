Clojure value finder (like `grep`)
==================================

Search for exact values in your Clojure project. No need to install. Try using through-the-web 1-liner.

![Usage](http://91.211.245.19/fo0001-usage.png)


E.g. search for a symbol
------------------------
```clojure
➜  > curl -s http://91.211.245.19/try-value-finder-ttw.sh | bash /dev/stdin "marker-start"
```
```
 ./fo0001/src/fo0001/finder.clj (def ^:dynamic marker-start "\u001b[31m\u001b[1m")
 ./fo0001/src/fo0001/finder.clj      marker-start
 ./fo0001/src/fo0001/base.clj             [fo0001.finder :refer [find-and-print marker-start marker-end]]
 ./fo0001/src/fo0001/base.clj               (when (clojure.string/includes? search-result marker-start)
 ./fo0001/src/fo0001/base.clj                                         start? (clojure.string/includes? line marker-start)
```

Parametric search -- prepend with `!`
-------------------------------------
E.g. serarch for all symbols, whose namespace is `"clojure.string"`:
```clojure
➜  > curl -s http://91.211.245.19/try-value-finder-ttw.sh | bash /dev/stdin '!(fn [x] (and (symbol? x) (-> x namespace (= "clojure.string"))))'
```
```
 ./fo0001/src/fo0001/print.clj           (print (clojure.string/join (repeat level "  "))) ; 2 spaces ident
 ./fo0001/src/fo0001/print.clj         (print (clojure.string/join (repeat level "  ")))
 ./fo0001/src/fo0001/base.clj         is-a-function? (clojure.string/starts-with? needle "!")
 ./fo0001/src/fo0001/base.clj                               (clojure.string/ends-with? (.getName file) ".clj")
 ./fo0001/src/fo0001/base.clj                               (clojure.string/ends-with? (.getName file) ".cljs")))
 ./fo0001/src/fo0001/base.clj               (when (clojure.string/includes? search-result marker-start)
 ./fo0001/src/fo0001/base.clj                 (let [lines (clojure.string/split-lines search-result)
 ./fo0001/src/fo0001/base.clj                                         start? (clojure.string/includes? line marker-start)
 ./fo0001/src/fo0001/base.clj                                         end? (clojure.string/includes? line marker-end)
```
