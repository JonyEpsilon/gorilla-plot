(defproject  pinkgorilla.ui.gorilla-plot "0.8.4"
  :description "A simple data-driven plotting library for Gorilla REPL."
  :url "https://github.com/pink-gorilla/gorilla-plot"
  :license {:name "MIT"}
   ;:deploy-repositories [["releases" :clojars]]
  :repositories [["clojars" {:url "https://clojars.org/repo"
                             :username "pinkgorillawb"
                             :sign-releases false}]]
  :dependencies 
  [[org.clojure/clojure "1.9.0-alpha14"]
   [pinkgorilla.ui.gorilla-renderable "2.0.9"] ;PinkGorilla Renderable (AND currently VEGA)
   ])
