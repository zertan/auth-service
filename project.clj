(defproject h-w "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [ring/ring-json "0.5.0"]
                 [compojure "1.6.1"]
                 [clj-http "3.10.1"]
                 [cheshire "5.10.0"]]
  :main ^:skip-aot h-w.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :source-paths ["src" "src.dev"]
                       :dependencies [[org.clojure/tools.nrepl "0.2.13"]
                                      [ring/ring-devel "1.8.1"]]}
             
             :dev {:source-paths ["src" "src.dev"]
                   :dependencies [[org.clojure/tools.nrepl "0.2.13"]
                                  [ring/ring-devel "1.8.1"]
]
}
             :prod {:source-paths ["src" "src.prod"]}})

