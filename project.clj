(defproject fo0001 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [rewrite-clj "0.6.2-SNAPSHOT"]
                 [mvxcvi/puget "1.0.3"]
                 [org.clojure/tools.cli "0.4.1"]]
  :plugins [[lein-cljfmt "0.6.1"]]
  :main fo0001.core
  :aot [fo0001.core])
