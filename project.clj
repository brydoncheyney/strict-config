(defproject strict-config "0.1.0-SNAPSHOT"
  :description "Less tolerant Clojure service configuration."
  :url "https://github.com/brydoncheyney/strict-config"
  :license {:name "MIT License"}
  :dependencies	[[org.clojure/clojure "1.5.1"]]
  :profiles {:dev
             {:plugins [[lein-midje "3.2.1"] ]
              :dependencies [[midje "1.8.3"]]}})
