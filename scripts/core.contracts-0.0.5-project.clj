(defproject core.contracts "0.0.5-SNAPSHOT"
  ;; This file is identical to project.clj from the core.contracts
  ;; github repo, except I have added the following line needed to run
  ;; lein-clojuredocs.
  :eval-in :leiningen
  :description "Contracts programming for Clojure."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.unify "0.5.3"]]
  :plugins [[lein-swank "1.4.4"]
            [lein-marginalia "0.7.1"]]
  :profiles {:1.2   {:dependencies [[org.clojure/clojure "1.2.0"]]}
             :1.2.1 {:dependencies [[org.clojure/clojure "1.2.1"]]}
             :1.3   {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.4   {:dependencies [[org.clojure/clojure "1.4.0"]]}}
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :source-paths ["src/main/clojure"]
  :test-paths   ["src/test/clojure"])
