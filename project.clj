(defproject strict-config "0.1.0"
  :description "Less tolerant Clojure service configuration."
  :url "https://github.com/brydoncheyney/strict-config"
  :license {:name "MIT License"}
  :dependencies	[[org.clojure/clojure "1.5.1"]]
  :signing {:gpg-key "3F43809AE125AE7217006EB0BED6059AE9D61E7C"}
  :profiles {:dev
             {:plugins [[lein-midje "3.2.1"] ]
              :dependencies [[midje "1.8.3"]]}})
