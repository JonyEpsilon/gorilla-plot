(ns pinkgorilla.ui.gorilla-plot.core
  "take the gorilla-plot functions, and wrap them so they return
   a the datatype that will trigger vega rendering"
  (:require
   [pinkgorilla.ui.gorilla-plot.plot :as p]
   ;[pinkgorilla.ui.gorilla-plot.vega]
   ;[pinkgorilla.ui.gorilla-renderable :refer [render]]
   ;[pinkgorilla.ui.vega :refer [vega! rendered-to-spec]]
   ))

;; .plot functions create only the vega spec.
;; The notebook users should be able to use all this functions without having
;; to wrap them in (vega! f) syntax.


(defn vega! [p]
  (with-meta [:vega p] {:R true}))

(defn wrap-vega [f]
  (fn [& p]
    (vega!
     (apply f p))))

;; (def plot p/plot)
(def plot (wrap-vega p/plot))
(def list-plot (wrap-vega p/list-plot))
(def bar-chart (wrap-vega p/bar-chart))
(def histogram (wrap-vega p/histogram))


;(def compose (wrap-vega p/compose))


;(defn unwrap [renderable]
;  (rendered-to-spec (render renderable)))

(defn unwrap [renderable]
   (second renderable)) ; [:vega data]

(defn compose
  [& plots]
  (let [plots-unwrapped (into [] (map unwrap plots))]
    (->> plots-unwrapped
         (apply p/compose)
         (vega!))))

(comment

  ; test to show how to wrap a function with optional parameters
  (defn myprint [& p]
    (apply println p))
  (myprint "a" "b" "c")
  (println "a" "b" "c")

  ; list-plot with results in differne tformats  
  (list-plot [1 2 3]) ; returns the reifi function
  (render (list-plot [1 2 3])) ;; The data structure how the frontend would render it; inside the spec
  (unwrap (list-plot [1 2 3])) ;; only the vega specification

  ;; composed plot 
  (unwrap
   (compose
    (list-plot [1 2 3])
    (list-plot [3 2 1]))))

