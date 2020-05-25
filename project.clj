(defproject authsvc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.8.1"]
                 [http-kit "2.3.0"]
                 [ring/ring-json "0.5.0"]
                 [bidi "2.1.3"]
                 [clj-http-lite "0.3.0"]
                 [cheshire "5.10.0"]
                 [environ "1.2.0"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [cider/cider-nrepl "0.25.0-alpha1"]
                 [ring/ring-devel "1.8.1"]
                 [javax.xml.bind/jaxb-api "2.3.1"]
                 [org.glassfish.jaxb/jaxb-runtime "2.3.1"]]
  :source-paths ["src" "src.dev"]
  :main ^:skip-aot authsvc.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
                   
             :dev {:source-paths ["src.dev"]
                   :dependencies [[org.clojure/tools.nrepl "0.2.13"]
                                  [cider/cider-nrepl "0.25.0-alpha1"]
                                  [ring/ring-devel "1.8.1"]
                                  [javax.xml.bind/jaxb-api "2.3.1"]
                                  [org.glassfish.jaxb/jaxb-runtime "2.3.1"]]}

             :prod {:source-paths ["src.prod"] ;; e.g. lein with-profile +test native-image
                    :native-image {:opts ["--initialize-at-build-time"
                                          ;;"-H:+ReportExceptionStackTraces"
                                          "--report-unsupported-elements-at-runtime"
                                          "--enable-url-protocols=http,https"]
                                   :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}})

