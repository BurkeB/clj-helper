(defproject wwsoftware/clj-helper "0.0.3.14"
  :description "Helper Functions for Clojure(-Script) Projects"
  :url "https://git.fh-muenster.de/ww-software/libraries/clj-helper"
  :license {:name "Eclipse Public License - v 2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.4"]
                 [org.clojure/tools.logging "1.3.0"]
                 [org.clj-commons/digest "1.4.100"]
                 [org.tukaani/xz "1.10"]]
  :plugins [[lein-codox "0.10.8"]]
  :codox {:output-path "target/doc"
          :source-uri "https://git.fh-muenster.de/ww-software/libraries/clj-helper/-/blob/master/{filepath}#L{line}"
          :metadata {:doc/format :markdown}})
