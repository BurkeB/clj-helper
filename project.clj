(defproject de.bburke/clj-helper "0.1.0"
  :description "Helper Functions for Clojure(-Script) Projects"
  :url "https://github.com/BurkeB/clj-helper"
  :license {:name "Eclipse Public License - v 2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.4"]
                 [org.clojure/tools.logging "1.3.0"]
                 [org.clj-commons/digest "1.4.100"]
                 [org.tukaani/xz "1.10"]]
  :plugins [[lein-codox "0.10.8"]]
  :codox {:output-path "target/doc"
          :source-uri "https://github.com/BurkeB/clj-helper/blob/master/{filepath}#L{line}"
          :metadata {:doc/format :markdown}})
