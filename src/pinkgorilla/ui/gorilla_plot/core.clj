(ns pinkgorilla.ui.gorilla-plot.core
  "take the gorilla-plot functions, and wrap them so they return
   a the datatype that will trigger vega rendering"
  (:require 
   [pinkgorilla.ui.gorilla-plot.plot :as p]
   [pinkgorilla.ui.vega :refer [vega!]]))


(def plot p/plot)


(defn wrap-vega [f]
  (fn [& p]
    (do 
      (println "calculating with params" p)
      (vega! 
       (apply f p)))))

;; 

(def list-plot (wrap-vega p/list-plot))
(def bar-chart (wrap-vega p/bar-chart))
(def histogram (wrap-vega p/histogram))
(def compose (wrap-vega p/compose))

(comment
  
  (defn myprint [& p]
    (apply println p))
  
  (myprint "a" "b" "c")
  (println "a" "b" "c")
  
  (list-plot [1 2 3])
  
  list-plot
  
  )

