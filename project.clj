(defproject novelette-sprite "0.1.0.2"
  :description "Clojurescript library for the rendering of animated sprites on canvas."
  :license "MIT License"
  :url "https://github.com/Morgawr/Novelette-sprite"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [prismatic/schema "1.0.4"]
                 [lein-doo "0.1.6"]]
  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-doo "0.1.6"]]
  :hooks [leiningen.cljsbuild]
  :clean-targets ["runtime/js/*"]
  :cljsbuild
  {
   :builds
   [
    {:id "novelette-sprite"
     :source-paths ["src/"]
     :compiler
     {:optimizations :simple
      :closure-output-charset "US-ASCII"
      :output-dir "runtime/js"
      :output-to  "runtime/js/novelette-sprite.js"
      :pretty-print true
      :source-map "runtime/js/novelette-sprite.js.map"}}]})
